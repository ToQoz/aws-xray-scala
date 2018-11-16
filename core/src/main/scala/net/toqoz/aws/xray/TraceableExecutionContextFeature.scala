package net.toqoz.aws.xray

import scala.concurrent.ExecutionContext
import com.amazonaws.xray.AWSXRay

trait TraceableExecutionContextFeature extends ExecutionContext { self =>
  override def prepare(): ExecutionContext = new ExecutionContext {
    private val callerTraceEntity = Option(AWSXRay.getTraceEntity)

    def execute(runnable: Runnable): Unit =
      self.execute(() => {
        val stash = Option(AWSXRay.getTraceEntity)
        try {
          callerTraceEntity match {
            case Some(e) => AWSXRay.setTraceEntity(e)
            case _       => AWSXRay.clearTraceEntity()
          }
          runnable.run()
        } finally {
          // Restore TraceEntity
          stash match {
            case Some(e) => AWSXRay.setTraceEntity(e)
            case _       => AWSXRay.clearTraceEntity()
          }
        }
      })

    def reportFailure(t: Throwable): Unit = self.reportFailure(t)
  }
}
