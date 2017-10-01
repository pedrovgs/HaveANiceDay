package extensions

import org.scalacheck.Gen

object scalacheck {

  implicit class RichGen[T](gen: Gen[T]) {
    def one(): T = {
      var sample = gen.sample
      while (!sample.isDefined) {
        sample = gen.sample
      }
      sample.get
    }
  }

}
