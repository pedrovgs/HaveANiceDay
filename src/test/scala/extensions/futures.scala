package extensions

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object futures {

  implicit class RichFuture[T](future: Future[T]) {
    def get: T = Await.result(future, Duration.Inf)
  }

}
