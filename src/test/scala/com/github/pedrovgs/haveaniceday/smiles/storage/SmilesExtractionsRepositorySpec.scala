package com.github.pedrovgs.haveaniceday.smiles.storage

import ordering.common._
import extensions.futures._
import generators.common._
import generators.smiles._
import org.joda.time.DateTime
import org.scalacheck.Gen
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
      val result = repository.saveLastExtractionStorage(extractionDate, smilesExtractedCount).get

      result shouldBe extractionDate
    }
  }

  it should "return the latest added extraction report date" in {
    forAll(Gen.nonEmptyListOf(arbitraryDateTime), arbitrarySmilesExtractedCount) { (extractionDates, count) =>
      extractionDates.foreach { date =>
        repository.saveLastExtractionStorage(date, count).get
      }

      repository.getLastSmilesExtraction.get.get shouldBe extractionDates.sorted(Ordering[DateTime].reverse).head
      resetDatabase()
    }
  }
}
