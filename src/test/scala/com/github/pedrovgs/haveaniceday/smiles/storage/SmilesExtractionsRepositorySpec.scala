package com.github.pedrovgs.haveaniceday.smiles.storage

import generators.common._
import generators.smiles._
import extensions.futures._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import specs.InMemoryDatabase

class SmilesExtractionsRepositorySpec
    extends FlatSpec
    with Matchers
    with InMemoryDatabase
    with BeforeAndAfter
    with PropertyChecks {

  private val repository = new SmilesExtractionsRepository(database)

  before {
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "SmilesExtractionsRepository" should "return a None last extraction date by default" in {
    val result = repository.getLastSmilesExtraction.get

    result shouldBe None
  }

  it should "update the last extraction report" in {
    forAll(arbitraryDateTime, arbitrarySmilesExtractedCount) { (extractionDate, smilesExtractedCount) =>
      val result = repository.updateLastExtractionStorage(extractionDate, smilesExtractedCount).get

      result shouldBe extractionDate
    }

  }
}
