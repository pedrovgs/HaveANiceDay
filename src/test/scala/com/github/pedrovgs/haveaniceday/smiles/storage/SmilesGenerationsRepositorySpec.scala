package com.github.pedrovgs.haveaniceday.smiles.storage

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGenerationResult
import com.github.pedrovgs.haveaniceday.utils.Clock
import extensions.futures._
import generators.smiles._
import org.scalacheck.Gen
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import specs.InMemoryDatabase

import scala.concurrent.Future

class SmilesGenerationsRepositorySpec
    extends FlatSpec
    with Matchers
    with InMemoryDatabase
    with BeforeAndAfter
    with PropertyChecks
    with MockitoSugar {

  private val repository       = new SmilesGenerationsRepository(database, new Clock)
  private val smilesRepository = new SmilesRepository(database)

  before {
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "SmilesGenerationsRepository" should "save an smile generation result" in {
    forAll(arbitrarySmilesGenerationResult) { result =>
      val savedResult = saveResult(result).get

      assertGenerationResultSavedProperly(result, savedResult)
      resetDatabase()
    }
  }

  it should "obtain previously saved generation results" in {
    forAll(Gen.nonEmptyListOf(arbitrarySmilesGenerationResult)) { results =>
      results.foreach(saveResult(_).get)

      val savedResults = repository.getGenerations().get

      savedResults.length shouldBe results.length
      resetDatabase()
    }
  }

  private def assertGenerationResultSavedProperly(result: SmilesGenerationResult,
                                                  savedResult: SmilesGenerationResult) = {
    result match {
      case Right(smile) => smile shouldBe savedResult.right.get.copy(id = smile.id)
      case error        => savedResult shouldBe error
    }
  }

  private def saveResult(result: SmilesGenerationResult): Future[SmilesGenerationResult] = {
    val resultToSave = result match {
      case Right(smile) =>
        val savedSmile = smilesRepository.saveSmiles(Seq(smile)).get.head
        Right(savedSmile)
      case Left(error) => Left(error)
    }
    repository.saveLastGenerationStorage(resultToSave)
  }
}
