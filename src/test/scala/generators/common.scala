package generators

import com.github.pedrovgs.haveaniceday.utils.model.Query
import finatra.api.model.PageApiModel
import org.joda.time.DateTime
import org.scalacheck.Gen
import org.scalacheck.Arbitrary._

object common {

  val arbitraryDateTime: Gen[DateTime] =
    Gen
      .choose(new DateTime().minusYears(10).toDate.getTime, new DateTime().plusYears(10).toDate.getTime)
      .map(new DateTime(_).withMillisOfSecond(0))

  val arbitraryId: Gen[Long] = arbitrary[Long].map(Math.abs(_) + 1)

  val arbitraryPositiveInt: Gen[Int] = arbitrary[Int].map(Math.abs)

  val arbitraryPositiveLong: Gen[Long] = arbitrary[Long].map(Math.abs)

  val arbitraryUrl: Gen[String] = {
    val arbitraryLongUrl = for {
      schema   <- Gen.oneOf("http", "https")
      host     <- Gen.alphaLowerStr.filter(!_.isEmpty)
      domain   <- Gen.alphaLowerStr.filter(!_.isEmpty)
      resource <- Gen.alphaLowerStr
    } yield schema + "://" + host + "." + domain + "/" + resource
    arbitraryLongUrl.filter(_.length <= 2083)
  }

  val arbitraryQuery: Gen[Query] =
    for {
      page     <- Gen.posNum[Long]
      pageSize <- Gen.posNum[Int]
    } yield Query(page, pageSize)

  def arbitraryStrMaxSize(maxSize: Int) =
    for {
      size  <- Gen.choose(0, maxSize)
      value <- Gen.listOfN(size, Gen.alphaNumChar)
    } yield value.mkString("")

  def arbitraryStrBetweenSize(minSize: Int, maxSize: Int) =
    for {
      size  <- Gen.choose(minSize, maxSize)
      value <- Gen.listOfN(size, Gen.alphaNumChar)
    } yield value.mkString("")

  def arbitraryPage[T](gen: Gen[T]): Gen[PageApiModel[T]] =
    for {
      pageSize   <- Gen.choose(1, 25)
      data       <- Gen.listOfN(pageSize, gen)
      page       <- arbitrary[Int]
      totalCount <- Gen.choose(pageSize, Long.MaxValue)
    } yield PageApiModel(data, totalCount, page, pageSize)
}
