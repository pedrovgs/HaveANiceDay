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

  object RichGen {
    def listOfMaxN[T](maxSize: Int, generator: Gen[T]): Gen[Seq[T]] =
      for {
        size <- Gen.choose(0, maxSize)
        list <- Gen.listOfN(size, generator)
      } yield list

    def nonEmptyListOfMaxN[T](maxSize: Int, generator: Gen[T]): Gen[Seq[T]] =
      listOfMaxN(maxSize, generator).filter(_.nonEmpty)
  }

}
