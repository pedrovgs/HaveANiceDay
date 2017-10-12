package generators

import org.joda.time.DateTime
import org.scalacheck.Gen
import org.scalacheck.Arbitrary._

object common {

  val arbitraryDateTime: Gen[DateTime] =
    Gen
      .choose(new DateTime().minusYears(10).toDate.getTime, new DateTime().plusYears(10).toDate.getTime)
      .map(new DateTime(_).withMillisOfSecond(0))

  val arbitraryId: Gen[Long] = arbitrary[Long].map(Math.abs(_) + 1)

  val arbitraryPositiveInt: Gen[Int] = arbitrary[Int].map(Math.abs(_) + 1)

  val arbitraryUrl: Gen[String] = {
    val arbitraryLongUrl = for {
      schema   <- Gen.oneOf("http", "https")
      host     <- Gen.alphaLowerStr.filter(!_.isEmpty)
      domain   <- Gen.alphaLowerStr.filter(!_.isEmpty)
      resource <- Gen.alphaLowerStr
    } yield schema + "://" + host + "." + domain + "/" + resource
    arbitraryLongUrl.filter(_.length <= 2083)
  }

  def arbitraryStrMaxSize(maxSize: Int) =
    for {
      size  <- Gen.choose(0, maxSize)
      value <- Gen.listOfN(size, Gen.alphaNumChar)
    } yield value.mkString("")
}
