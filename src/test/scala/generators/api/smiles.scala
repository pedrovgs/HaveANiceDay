package generators.api

import com.github.pedrovgs.haveaniceday.smiles.utils.SmileTitleGenerator
import finatra.api.smiles.model.SmileApiModel
import generators.common._
import org.scalacheck.Gen

object smiles {

  val arbitrarySmileApiModel: Gen[SmileApiModel] = for {
    id          <- Gen.posNum[Long]
    smileNumber <- Gen.posNum[Int]
    message     <- Gen.option(Gen.alphaNumStr)
    photoUrl    <- Gen.option(arbitraryUrl)
    title = SmileTitleGenerator.generateSmileForTitle(smileNumber)
  } yield SmileApiModel(id, title, message, photoUrl)

}
