package com.github.pedrovgs.haveaniceday.smiles

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import generators.smiles._
import extensions.futures._
import com.github.pedrovgs.haveaniceday.smiles.storage.SmilesRepository
import com.github.pedrovgs.haveaniceday.utils.model.ItemNotFound
import org.scalacheck.Arbitrary._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.prop.PropertyChecks
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import specs.InMemoryDatabase

class GetSmileByIdSpec
    extends FlatSpec
    with Matchers
    with InMemoryDatabase
    with BeforeAndAfter
    with PropertyChecks
    with MockitoSugar {

  private val repository = new SmilesRepository(database)
  private val getSmile   = new GetSmileById(repository)

  before {
    resetDatabase()
  }

  after {
    resetDatabase()
  }

  "GetSmileById" should "return item not found error if the smile does not exist" in {
    forAll(arbitrary[Long]) { id =>
      val result = getSmile(id).awaitForResult

      result shouldBe Left(ItemNotFound(id.toString))
    }
  }

  it should "return item not found if the smile exist but it was not sent" in {
    forAll(arbitraryNotSentSmile) { smile =>
      val id = saveSmile(smile).id

      val result = getSmile(id).awaitForResult

      result shouldBe Left(ItemNotFound(id.toString))
    }
  }

  it should "return the smile associated with the id passed as parameter" in {
    forAll(arbitrarySentSmile) { smile =>
      val savedSmile = saveSmile(smile)

      val result = getSmile(savedSmile.id).awaitForResult

      result shouldBe Right(savedSmile)
    }
  }

  private def saveSmile(smile: Smile): Smile = repository.saveSmiles(Seq(smile)).awaitForResult.head

}
