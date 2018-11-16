package net.toqoz.aws.xray.play

import scala.concurrent.ExecutionContext
import com.amazonaws.xray.AWSXRay
import com.amazonaws.xray.entities.Entity
import net.toqoz.aws.xray.TraceableExecutionContext
import play.api.libs.typedmap.TypedKey
import play.api.mvc.RequestHeader

object AWSXRayPlayContext {
  private val TraceEntityAttr: TypedKey[Entity] = TypedKey("net.toqoz.aws.xray.play.TraceEntity")

  def addTraceEntity(request: RequestHeader, entity: Entity): RequestHeader = {
    request.addAttr(TraceEntityAttr, entity)
  }

  def withTraceEntity[T](ec: ExecutionContext)(action: ExecutionContext => T)(implicit request: RequestHeader): T = {
    val stash = Option(AWSXRay.getTraceEntity)
    val traceEntity = request.attrs.get(TraceEntityAttr)
    traceEntity match {
      case Some(e) => AWSXRay.setTraceEntity(e)
      case _       => AWSXRay.clearTraceEntity()
    }
    try {
      action(new TraceableExecutionContext(ec))
    } finally {
      if (traceEntity.isDefined) {
        stash match {
          case Some(e) => AWSXRay.setTraceEntity(e)
          case _       => AWSXRay.clearTraceEntity()
        }
      }
    }
  }
}
