package com.github.pedrovgs.haveaniceday.smiles.storage

import com.github.pedrovgs.haveaniceday.smiles.model
import extensions.futures._
import generators.smiles._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import specs.InMemoryDatabase
import org.scalacheck.Gen

class SmilesRepositorySpec
    extends FlatSpec
    with Matchers
    with InMemoryDatabase
    with BeforeAndAfter
    with PropertyChecks {

  private val repository = new SmilesRepository(database)

  before {
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "SmilesRepository" should "save smiles into the database" in {
    forAll(Gen.nonEmptyListOf(arbitrarySmile)) { smiles =>
      val savedSmiles = repository.saveSmiles(smiles).awaitForResult

      assertSmilesAreSavedProperly(smiles, savedSmiles)
    }
  }

  private def assertSmilesAreSavedProperly(smiles: List[model.Smile], savedSmiles: Seq[model.Smile]): Unit = {
    val sortedSmiles      = smiles.sortBy(_.sourceUrl)
    val sortedSavedSmiles = savedSmiles.sortBy(_.sourceUrl)
    sortedSmiles.length shouldBe sortedSavedSmiles.length
    sortedSmiles.zip(sortedSavedSmiles).foreach { tuple =>
      val smile = tuple._1.copy(id = tuple._2.id)
      smile shouldBe tuple._2
    }
  }
}
