package com.github.pedrovgs.haveaniceday.smiles

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.storage.SmilesRepository
import com.github.pedrovgs.haveaniceday.utils.model.ItemNotFound
import extensions.futures._
import extensions.scalacheck.{RichGen, _}
import generators.smiles._
import org.scalacheck.Gen
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import specs.InMemoryDatabase

class GetRandomSmileSpec
    extends FlatSpec
    with Matchers
    with InMemoryDatabase
    with BeforeAndAfter
    with PropertyChecks
    with MockitoSugar {

  private val repository     = new SmilesRepository(database)
  private val getRandomSmile = new GetRandomSmile(repository)

  before {
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "GetRandomSmile" should "return item not found if there are no smiles" in {
    val result = getRandomSmile().awaitForResult

    result shouldBe Left(ItemNotFound())
  }

  it should "return item not found if there are no sent smiles" in {
    forAll(Gen.listOf(arbitraryNotSentSmile)) { smiles =>
      saveSmiles(smiles)

      val result = getRandomSmile().awaitForResult

      result shouldBe Left(ItemNotFound())
      resetDatabase()
    }
  }

  it should "return a random smile if there are smiles sent" in {
    forAll(RichGen.nonEmptyListOfMaxN(10, arbitrarySentSmile)) { smiles =>
      val savedSmiles = saveSmiles(smiles)

      val result = getRandomSmile().awaitForResult.right.get

      result.sent shouldBe true
      savedSmiles.contains(result) shouldBe true
      resetDatabase()
    }
  }

  it should "return a random smile obtained from the list of smiles sent" in {
    forAll(RichGen.nonEmptyListOfMaxN(10, arbitrarySentSmile), RichGen.nonEmptyListOfMaxN(10, arbitraryNotSentSmile)) {
      (smilesSent, smilesNotSent) =>
        val savedSmilesSent    = saveSmiles(smilesSent)
        val savedSmilesNotSent = saveSmiles(smilesNotSent)

        val result = getRandomSmile().awaitForResult.right.get

        result.sent shouldBe true
        savedSmilesSent.contains(result) shouldBe true
        savedSmilesNotSent.contains(result) shouldBe false
        resetDatabase()
    }
  }

  private def saveSmiles(smiles: Seq[Smile]): Seq[Smile] = repository.saveSmiles(smiles).awaitForResult

}
