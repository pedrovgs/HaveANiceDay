package com.github.pedrovgs.haveaniceday.smiles.storage

import org.scalatest.{AsyncFlatSpec, Matchers}
import specs.InMemoryDatabase

class SmilesExtractionsRepositorySpec extends AsyncFlatSpec with Matchers with InMemoryDatabase {

  private val repository = new SmilesExtractionsRepository(database)

  "SmilesExtractionsRepository" should "return a None last extraction date by default" in {
    repository.getLastSmilesExtraction.map { lastExtractionDate =>
      lastExtractionDate shouldBe None
    }
  }
}
