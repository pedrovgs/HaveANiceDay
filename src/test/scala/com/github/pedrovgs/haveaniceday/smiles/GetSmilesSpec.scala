package com.github.pedrovgs.haveaniceday.smiles

import ordering.common._
import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.storage.SmilesRepository
import com.github.pedrovgs.haveaniceday.utils.model.{InvalidQuery, Query, QueryResult}
import extensions.futures._
import generators.common._
import generators.smiles._
import org.scalacheck.Gen
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import specs.InMemoryDatabase

class GetSmilesSpec
    extends FlatSpec
    with Matchers
    with InMemoryDatabase
    with BeforeAndAfter
    with PropertyChecks
    with MockitoSugar {

  private val repository     = new SmilesRepository(database)
  private val getSmiles      = new GetSmiles(repository)
  private val firstPageQuery = Query()

  before {
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "GetSmiles" should "return an empty result if there are no smiles" in {
    forAll(arbitraryQuery) { query =>
      val result = getSmiles(query).awaitForResult.right.get

      result shouldBe QueryResult(query, Seq(), 0)
    }
  }

  it should "return an empty result if there are no smiles sent" in {
    forAll(Gen.nonEmptyListOf(arbitraryNotSentSmile)) { smiles =>
      saveSmiles(smiles)

      val result = getSmiles(firstPageQuery).awaitForResult.right.get

      result shouldBe QueryResult(firstPageQuery, Seq(), 0)
      resetDatabase()
    }
  }

  it should "return every smile in the repository if every is smile is sent" in {
    val numberOfSmiles = 10
    val query          = Query(1, numberOfSmiles)
    forAll(Gen.listOfN(numberOfSmiles, arbitrarySentSmile)) { smiles =>
      val savedSmiles = saveSmiles(smiles)

      val result = getSmiles(query).awaitForResult.right.get

      val expectedResult = QueryResult(query, savedSmiles.sortBy(_.sentDate).reverse.toVector, numberOfSmiles)
      result shouldBe expectedResult
      resetDatabase()
    }
  }

  it should "return just sent smiles" in {
    forAll(Gen.nonEmptyListOf(arbitrarySmile)) { smiles =>
      saveSmiles(smiles)

      val result = getSmiles(firstPageQuery).awaitForResult.right.get

      result.data.forall(_.sent) shouldBe true
      result.data.forall(_.sentDate.isDefined) shouldBe true
      result.data.forall(_.number.isDefined) shouldBe true
      resetDatabase()
    }
  }

  it should "return an empty result if there are no more smiles in the next page" in {
    val numberOfSmiles = 10
    val query          = Query(2, numberOfSmiles)
    forAll(Gen.listOfN(numberOfSmiles, arbitrarySentSmile)) { smiles =>
      saveSmiles(smiles)

      val result = getSmiles(query).awaitForResult.right.get

      val expectedResult = QueryResult(query, Seq(), numberOfSmiles)
      result shouldBe expectedResult
      resetDatabase()
    }
  }

  it should "return just a bunch of smiles even if there are less smiles than the total number requested" in {
    val numberOfSmiles = 10
    forAll(Gen.listOfN(numberOfSmiles, arbitrarySentSmile)) { smiles =>
      saveSmiles(smiles)

      val query  = Query(numberOfSmiles * 2)
      val result = getSmiles(query).awaitForResult.right.get

      val expectedResult = QueryResult(query, Seq(), numberOfSmiles)
      result shouldBe expectedResult
      resetDatabase()
    }
  }

  it should "return an InvalidQuery error if the page is 0" in {
    val query = Query(0)

    val result = getSmiles(query).awaitForResult.left.get

    result shouldBe InvalidQuery("The page passed as parameter possible values are between 1 and the max Long value")
  }

  it should "return an InvalidQuery error if the page is negative" in {
    forAll(Gen.negNum[Int]) { page =>
      val query = Query(page)

      val result = getSmiles(query).awaitForResult.left.get

      result shouldBe InvalidQuery("The page passed as parameter possible values are between 1 and the max Long value")
    }
  }

  it should "return an InvalidQuery error if the pageSize is 0" in {
    val query = Query(pageSize = 0)

    val result = getSmiles(query).awaitForResult.left.get

    result shouldBe InvalidQuery("The page size can't be negative or zero")
  }

  it should "return an InvalidQuery error if the pageSize is negative" in {
    forAll(Gen.negNum[Int]) { pageSize =>
      val query = Query(pageSize = pageSize)

      val result = getSmiles(query).awaitForResult.left.get

      result shouldBe InvalidQuery("The page size can't be negative or zero")
    }
  }

  it should "return an InvalidQuery error if the pageSize is greater or equal to 100" in {
    forAll(Gen.choose(100, Int.MaxValue)) { pageSize =>
      val query = Query(pageSize = pageSize)

      val result = getSmiles(query).awaitForResult.left.get

      result shouldBe InvalidQuery("The page size can't be greater than 100")
    }
  }

  private def saveSmiles(smiles: Seq[Smile]): Seq[Smile] = repository.saveSmiles(smiles).awaitForResult

}
