package generators

import extensions.scalacheck._
import com.github.pedrovgs.haveaniceday.smiles.model._
import generators.common._
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

object smiles {

  val arbitrarySmileNumber: Gen[Int] = Gen.choose(0, Int.MaxValue)

  val arbitrarySmilesExtractedCount: Gen[Int] = Gen.choose(0, Int.MaxValue)

  val arbitrarySmile: Gen[Smile] = for {
    id            <- arbitraryId
    creationDate  <- arbitraryDateTime
    photo         <- Gen.option(arbitraryUrl)
    description   <- Gen.option(arbitraryStrMaxSize(280))
    source        <- Gen.oneOf(Source.values.toSeq)
    sourceUrl     <- arbitraryUrl
    numberOfLikes <- arbitraryPositiveLong
    sent          <- arbitrary[Boolean]
    sentDate      <- if (sent) Gen.some(arbitraryDateTime) else Gen.const(None)
    number        <- if (sent) Gen.some(arbitraryPositiveInt) else Gen.const(None)
  } yield Smile(id, creationDate, photo, description, source, sourceUrl, numberOfLikes, sent, sentDate, number)

  val arbitraryNotSentSmile: Gen[Smile] = arbitrarySmile.map(_.copy(sent = false, sentDate = None, number = None))

  val arbitrarySentSmile: Gen[Smile] = arbitrarySmile.map(_.copy(sent = true, sentDate = Gen.some(arbitraryDateTime).one, number = Gen.some(Gen.posNum[Int]).one))

  val arbitrarySmilesExtractionError: Gen[SmilesExtractionError] =
    arbitrary[String].map(UnknownError)

  val arbitrarySmilesGenerationError: Gen[SmilesGenerationError] =
    Gen.oneOf(NoExtractedSmilesFound, UnknownError("Ups something went wrong!"))

  val arbitrarySmilesGenerationResult: Gen[SmilesGenerationResult] = for {
    smile <- Gen.option(arbitrarySmile)
    error <- arbitrarySmilesGenerationError
  } yield
    smile match {
      case Some(smileSent) => Right(smileSent)
      case _               => Left(error)
    }
}
