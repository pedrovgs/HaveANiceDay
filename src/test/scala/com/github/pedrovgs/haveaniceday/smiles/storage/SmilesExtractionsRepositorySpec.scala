package com.github.pedrovgs.haveaniceday.smiles.storage

import org.scalatest.{AsyncFlatSpec, BeforeAndAfter, Matchers}
import specs.InMemoryDatabase

class SmilesExtractionsRepositorySpec extends AsyncFlatSpec with Matchers with InMemoryDatabase with BeforeAndAfter {

  private val repository = new SmilesExtractionsRepository(database)

  before {
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "SmilesExtractionsRepository" should "return a None last extraction date by default" in {
    repository.getLastSmilesExtraction.map { lastExtractionDate =>
      lastExtractionDate shouldBe None
    }
  }
}
