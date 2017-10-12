package generators

import com.github.pedrovgs.haveaniceday.smiles.model.{Smile, Source}
import generators.common._
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

object smiles {

  val arbitrarySmilesExtractedCount: Gen[Int] = Gen.choose(0, Int.MaxValue)

  val arbitrarySmile: Gen[Smile] = for {
    id           <- arbitraryId
    creationDate <- arbitraryDateTime
    photo        <- Gen.option(arbitraryUrl)
    description  <- Gen.option(arbitraryStrMaxSize(280))
    source       <- Gen.oneOf(Source.values.toSeq)
    sourceUrl    <- arbitraryUrl
    sent         <- arbitrary[Boolean]
    sentDate     <- if (sent) Gen.some(arbitraryDateTime) else Gen.const(None)
    number       <- if (sent) Gen.some(arbitraryPositiveInt) else Gen.const(None)
  } yield Smile(id, creationDate, photo, description, source, sourceUrl, sent, sentDate, number)

}
