package app.filters

import java.util.concurrent.Executors

import akka.stream.Materializer
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{
  ExecutionContext,
  ExecutionContextExecutorService,
  Future
}

@Singleton
class PreFilter @Inject()(implicit val mat: Materializer) extends Filter {
  implicit val ec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))

  def apply(next: RequestHeader => Future[Result])(
      requestHeader: RequestHeader): Future[Result] = {
    for {
      _ <- Future {
        Logger.info(s"Begin: PreFilter ${Thread.currentThread().getId}")
        Thread.sleep(1000)
      }
      n <- next(requestHeader)
      _ <- Future {
        Logger.info(s"End: PreFilter ${Thread.currentThread().getId}")
      }
    } yield n
  }
}
