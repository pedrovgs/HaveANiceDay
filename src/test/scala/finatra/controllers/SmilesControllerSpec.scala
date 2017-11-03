package finatra.controllers

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.{GetRandomSmile, GetSmileById, GetSmiles}
import com.github.pedrovgs.haveaniceday.utils.model.{HaveANiceDayError, ItemNotFound}
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import extensions.scalacheck._
import finatra.HaveANiceDayServer
import finatra.api.smiles.model._
import generators.smiles._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mockito.MockitoSugar
import slick.Database
import specs.JsonParser

import scala.concurrent.Future

class SmilesControllerSpec extends FeatureTest with MockitoSugar with JsonParser {

  private val getSmile = mock[GetSmileById]
  private val getRandomSmile = mock[GetRandomSmile]
  private val getSmiles = mock[GetSmiles]

  override val server: EmbeddedHttpServer = new EmbeddedHttpServer(new HaveANiceDayServer)
    .bind[Database](mock[Database])
    .bind[GetSmileById](getSmile)
    .bind[GetRandomSmile](getRandomSmile)
    .bind[GetSmiles](getSmiles)

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

  test("should return bad request if the id is not an integer or a long") {
    server.httpGet(path = "/api/smile/a", andExpect = Status.BadRequest)
  }

  test("should return not found if there are no sent smiles when asking for a random smile") {
    givenThereAreNoSmilesSent()

    server.httpGet(path = s"/api/smile", andExpect = Status.NotFound)
  }

  test("should return a random smile already sent") {
    val smile = givenThereIsARandomSmile()

    val expectedBody = mapper.writeValueAsString(smile)
    server.httpGet(path = s"/api/smile", andExpect = Status.Ok, withJsonBody = expectedBody)
  }

  def givenThereIsARandomSmile(): SmileApiModel = {
    val smile = arbitrarySentSmile.one
    when(getRandomSmile.apply()).thenReturn(Future.successful(Right(smile)))
    asApiModel(smile)
  }

  def givenThereAreNoSmilesSent(): Unit = {
    val result = Future.successful(Left(ItemNotFound()))
    when(getRandomSmile.apply()).thenReturn(result)
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
