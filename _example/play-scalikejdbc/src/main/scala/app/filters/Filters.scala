package app.filters
import akka.stream.Materializer
import com.amazonaws.xray.strategy.sampling.AllSamplingStrategy
import com.amazonaws.xray.AWSXRayRecorder
import javax.inject.{ Inject, Singleton }
import net.toqoz.aws.xray.play.AWSXRayPlayFilter
import play.api.Environment
import play.api.http.HttpFilters
import play.api.mvc.Filter

import scala.concurrent.ExecutionContext

@Singleton
class Filters @Inject()(
  env: Environment,
  preFilter: PreFilter,
  postFilter: PostFilter
)(implicit val mat: Materializer, ec: ExecutionContext)
    extends HttpFilters {
  override val filters: Seq[Filter] = Seq(
    preFilter,
    new AWSXRayPlayFilter(fixedSegmentName = "fallback-segment-name", recorder = newRecorder()),
    postFilter
  )

  private def newRecorder(): AWSXRayRecorder = {
    val rec = new AWSXRayRecorder
    rec.setSamplingStrategy(new AllSamplingStrategy())
    rec
  }
}
