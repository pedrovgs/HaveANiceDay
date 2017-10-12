package generators

import org.scalacheck.Gen

object smiles {

  val arbitrarySmilesExtractedCount: Gen[Int] = Gen.choose(0, Int.MaxValue)

}
