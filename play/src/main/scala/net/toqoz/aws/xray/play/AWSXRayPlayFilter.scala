package net.toqoz.aws.xray.play

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try
import java.util.{ HashMap => JavaHashMap }

import akka.stream.Materializer
import com.amazonaws.xray.{ AWSXRay, AWSXRayRecorder }
import com.amazonaws.xray.entities.{ Entity, Segment, TraceHeader, TraceID }
import com.amazonaws.xray.entities.TraceHeader.SampleDecision
import com.amazonaws.xray.strategy.sampling.{ SamplingRequest, SamplingResponse }
import com.typesafe.scalalogging.LazyLogging
import net.toqoz.aws.xray.config.SegmentNameConfig
import play.api.mvc.{ Filter, RequestHeader, Result }

class AWSXRayPlayFilter(fixedSegmentName: String, recorder: AWSXRayRecorder = AWSXRay.getGlobalRecorder)(implicit val mat: Materializer, ec: ExecutionContext)
    extends Filter
    with LazyLogging {
  private lazy val segmentName: String = SegmentNameConfig.name(fixedSegmentName)

  def apply(next: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    logger.debug("Beginning to process request: " + requestHeader.toString)
    val (traceEntity, responseTraceHeader) = createSegment(requestHeader)
    // NOTE:  set Entity to request attributes for async procedures
    //        https://docs.aws.amazon.com/xray/latest/devguide/scorekeep-workerthreads.html
    val newRequest = AWSXRayPlayContext.addTraceEntity(requestHeader, traceEntity)
    next(newRequest).map(endSegment(requestHeader, responseTraceHeader, traceEntity))
  }

  private def createSegment(requestHeader: RequestHeader): (Entity, TraceHeader) = {
    // Begin segment
    val maybeTraceHeader = getTraceHeader(requestHeader)
    val parentId = maybeTraceHeader.flatMap(x => Option(x.getParentId)).orNull

    val samplingStrategy = recorder.getSamplingStrategy
    val samplingResponse = fromSamplingStrategy(requestHeader)
    var sampleDecision = maybeTraceHeader.map(_.getSampled).getOrElse(getSampleDecision(samplingResponse))
    if (SampleDecision.REQUESTED == sampleDecision || SampleDecision.UNKNOWN == sampleDecision) sampleDecision = getSampleDecision(samplingResponse)

    val traceId = maybeTraceHeader.flatMap(x => Option(x.getRootTraceId)).getOrElse(new TraceID())

    val segment =
      if (SampleDecision.SAMPLED == sampleDecision) {
        val created = recorder.beginSegment(segmentName, traceId, parentId)
        if (samplingResponse.getRuleName.isPresent) {
          logger.debug("Sampling strategy decided to use rule named: " + samplingResponse.getRuleName.get + ".")
          created.setRuleName(samplingResponse.getRuleName.get)
        }
        created
      } else { //NOT_SAMPLED
        if (samplingStrategy.isForcedSamplingSupported) {
          val created = recorder.beginSegment(segmentName, traceId, parentId)
          created.setSampled(false)
          created
        } else {
          recorder.beginDummySegment(traceId)
        }
      }

    segment.putHttp("request", buildRequestAttributes(requestHeader))

    // Generate TraceHeader for response
    val responseTraceHeader = maybeTraceHeader match {
      case Some(traceHeader) =>
        val n = new TraceHeader(segment.getTraceId)
        if (SampleDecision.REQUESTED == traceHeader.getSampled) {
          traceHeader.setSampled(
            if (segment.isSampled) SampleDecision.SAMPLED
            else SampleDecision.NOT_SAMPLED
          )
        }
        n
      case _ => new TraceHeader(segment.getTraceId)
    }

    val traceEntity = recorder.getTraceEntity
    recorder.clearTraceEntity()
    (traceEntity, responseTraceHeader)
  }

  private def endSegment(requestHeader: RequestHeader, responseTraceHeader: TraceHeader, traceEntity: Entity)(result: Result): Result = {
    // set Entity to ThreadLocal
    recorder.setTraceEntity(traceEntity)
    val segment = recorder.getCurrentSegment
    try {
      result.header.status / 100 match {
        case 4 =>
          segment.setError(true)
          if (result.header.status == 429) {
            segment.setThrottle(true)
          }
        case 5 =>
          segment.setFault(true)
        case _ => ;
      }
      segment.putHttp("response", buildResponseAttributes(result))
      recorder.endSegment()
    } finally {
      recorder.clearTraceEntity()
      logger.debug("Finished processing request: " + requestHeader.toString)
    }

    // Response TraceHeader
    result.withHeaders(TraceHeader.HEADER_KEY -> responseTraceHeader.toString)
  }

  private def getTraceHeader(requestHeader: RequestHeader): Option[TraceHeader] = {
    requestHeader.headers.get(TraceHeader.HEADER_KEY) match {
      case Some(traceHeaderString) =>
        Option(TraceHeader.fromString(traceHeaderString))
      case _ => None
    }
  }

  private def fromSamplingStrategy(request: RequestHeader) = {
    val samplingRequest = new SamplingRequest(
      segmentName,
      request.host,
      request.uri,
      request.method,
      recorder.getOrigin
    )
    recorder.getSamplingStrategy.shouldTrace(samplingRequest)
  }

  private def getSampleDecision(sample: SamplingResponse) =
    if (sample.isSampled) {
      logger.debug("Sampling strategy decided SAMPLED.")
      SampleDecision.SAMPLED
    } else {
      logger.debug("Sampling strategy decided NOT_SAMPLED.")
      SampleDecision.NOT_SAMPLED
    }

  private def buildRequestAttributes(requestHeader: RequestHeader) = {
    val requestAttributes = new JavaHashMap[String, Any]()
    requestAttributes.put("url", requestHeader.uri)
    requestAttributes.put("method", requestHeader.method)
    requestHeader.headers.get("User-Agent").foreach { v =>
      requestAttributes.put("user_agent", v)
    }
    requestHeader.headers.get("X-Forwarded-For") match {
      case Some(v) =>
        v.split(",").headOption.foreach { f =>
          requestAttributes.put("client_ip", f.trim)
          requestAttributes.put("x_forwarded_for", true)
        }
      case _ =>
        requestAttributes.put("client_ip", requestHeader.remoteAddress)
    }
    requestAttributes
  }

  private def buildResponseAttributes(result: Result) = {
    val responseAttributes = new JavaHashMap[String, Any]()
    val status = result.header.status
    responseAttributes.put("status", status)
    result.header.headers.get("Content-Length").foreach { v =>
      Try(v.toInt).toOption.foreach { i =>
        responseAttributes.put("content_length", i)
      }
    }
    responseAttributes
  }
}
