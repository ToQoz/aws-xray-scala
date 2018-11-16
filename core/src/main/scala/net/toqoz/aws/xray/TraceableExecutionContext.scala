package net.toqoz.aws.xray

import scala.concurrent.ExecutionContext

class TraceableExecutionContext(ec: ExecutionContext) extends ExecutionContext with TraceableExecutionContextFeature {
  override def execute(runnable: Runnable): Unit = ec.execute(runnable)
  override def reportFailure(cause: Throwable): Unit = ec.reportFailure(cause)
}
