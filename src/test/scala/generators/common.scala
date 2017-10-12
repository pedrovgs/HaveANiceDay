package generators

import org.joda.time.DateTime
import org.scalacheck.Gen

object common {

  val arbitraryDateTime: Gen[DateTime] =
    Gen
      .choose(new DateTime().minusYears(10).toDate.getTime, new DateTime().plusYears(10).toDate.getTime)
      .map(new DateTime(_).withMillisOfSecond(0))

}
