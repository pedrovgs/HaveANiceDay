package finatra.controllers

import com.github.pedrovgs.haveaniceday.smiles.GetSmileById
import com.github.pedrovgs.haveaniceday.utils.model.ItemNotFound
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import extensions.scalacheck._
import finatra.HaveANiceDayServer
import finatra.api.smiles.model._
import generators.smiles._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import slick.Database
import specs.JsonParser

import scala.concurrent.Future

class SmilesControllerSpec extends FeatureTest with MockitoSugar with JsonParser {

  private val getSmile = mock[GetSmileById]

  override val server: EmbeddedHttpServer = new EmbeddedHttpServer(new HaveANiceDayServer)
    .bind[Database](mock[Database])
    .bind[GetSmileById](getSmile)

  test("should return not found if the smile does not exist when getting it by id") {
    val id = 1
    givenTheSmileDoesNotExist(id)

    server.httpGet(path = s"/api/smile/$id", andExpect = Status.NotFound)
  }

  test("should return the smile if it does exist when getting it by id") {
    val id    = 1
    val smile = givenTheSmileExists(id)

    val expectedBody = mapper.writeValueAsString(smile)
    server.httpGet(path = s"/api/smile/$id", andExpect = Status.Ok, withJsonBody = expectedBody)
  }

  private def givenTheSmileDoesNotExist(id: Long) = {
    val result = Future.successful(Left(ItemNotFound(id.toString)))
    when(getSmile.apply(id)).thenReturn(result)
  }

  private def givenTheSmileExists(id: Long): SmileApiModel = {
    val smile  = arbitrarySentSmile.one
    val result = Future.successful(Right(smile))
    when(getSmile.apply(id)).thenReturn(result)
    smile
  }

}
