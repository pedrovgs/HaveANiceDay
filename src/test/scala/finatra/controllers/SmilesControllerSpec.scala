package finatra.controllers

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.{GetRandomSmile, GetSmileById, GetSmiles}
import com.github.pedrovgs.haveaniceday.utils.model.{InvalidQuery, ItemNotFound, Query, QueryResult}
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import extensions.scalacheck._
import finatra.HaveANiceDayServer
import finatra.api.model.PageApiModel
import finatra.api.smiles.model._
import generators.smiles._
import org.mockito.Mockito._
import org.scalacheck.Gen
import org.scalatest.mockito.MockitoSugar
import slick.Database
import specs.JsonParser

import scala.concurrent.Future

class SmilesControllerSpec extends FeatureTest with MockitoSugar with JsonParser {

  private val smilePath      = "/api/smile"
  private val smilesPath     = "/api/smiles"
  private val firstPageQuery = Query()

  private val getSmile       = mock[GetSmileById]
  private val getRandomSmile = mock[GetRandomSmile]
  private val getSmiles      = mock[GetSmiles]

  override val server: EmbeddedHttpServer = new EmbeddedHttpServer(new HaveANiceDayServer)
    .bind[Database](mock[Database])
    .bind[GetSmileById](getSmile)
    .bind[GetRandomSmile](getRandomSmile)
    .bind[GetSmiles](getSmiles)

  test("should return not found if the smile does not exist when getting it by id") {
    val id = 1
    givenTheSmileDoesNotExist(id)

    server.httpGet(path = s"$smilePath/$id", andExpect = Status.NotFound)
  }

  test("should return the smile if it does exist when getting it by id") {
    val id    = 1
    val smile = givenTheSmileExists(id)

    val expectedBody = mapper.writeValueAsString(smile)
    server.httpGet(path = s"$smilePath/$id", andExpect = Status.Ok, withJsonBody = expectedBody)
  }

  test("should return bad request if the id is not an integer or a long") {
    val id = "a"
    server.httpGet(path = smileByIdPath(id), andExpect = Status.BadRequest)
  }

  test("should return not found if there are no sent smiles when asking for a random smile") {
    givenThereAreNoSmiles()

    server.httpGet(path = smilePath, andExpect = Status.NotFound)
  }

  test("should return a random smile already sent") {
    val smile = givenThereIsARandomSmile()

    val expectedBody = mapper.writeValueAsString(smile)
    server.httpGet(path = smilePath, andExpect = Status.Ok, withJsonBody = expectedBody)
  }

  test("uses page 1 and 25 as page size by default if these params are not specified") {
    givenThereAreNoSmiles(firstPageQuery)

    server.httpGet(path = smilesPath, andExpect = Status.Ok)

    verify(getSmiles).apply(Query(1, 25))
  }

  test("should return an empty page if there are no smiles") {
    val page = givenThereAreNoSmiles(firstPageQuery)

    val expectedBody = mapper.writeValueAsString(page)
    server.httpGet(path = getSmilesPath(firstPageQuery), andExpect = Status.Ok, withJsonBody = expectedBody)
  }

  test("should return the page obtained from our server") {
    val page = givenThereAreSomeSmiles(firstPageQuery)

    val expectedBody = mapper.writeValueAsString(page)
    server.httpGet(path = getSmilesPath(firstPageQuery), andExpect = Status.Ok, withJsonBody = expectedBody)
  }

  test("should return an error if there is somethig wrong in our request") {
    val errorMessage = "There is something wrong in your query"
    givenTheQueryIsMalformed(firstPageQuery, errorMessage)

    server.httpGet(path = getSmilesPath(firstPageQuery),
                   andExpect = Status.BadRequest,
                   withJsonBody = "There is something wrong in your query")
  }

  private def givenTheQueryIsMalformed(query: Query, message: String) = {
    val result = Future.successful(Left(InvalidQuery(message)))
    when(getSmiles.apply(query)).thenReturn(result)
  }

  private def getSmilesPath(query: Query): String =
    s"$smilesPath?page=${query.page}&pageSize=${query.pageSize}"

  private def givenThereIsARandomSmile(): SmileApiModel = {
    val smile = arbitrarySentSmile.one
    when(getRandomSmile.apply()).thenReturn(Future.successful(Right(smile)))
    asApiModel(smile)
  }

  private def givenThereAreNoSmiles(): Unit = {
    val result = Future.successful(Left(ItemNotFound()))
    when(getRandomSmile.apply()).thenReturn(result)
  }

  private def givenThereAreNoSmiles(query: Query): PageApiModel[SmileApiModel] = {
    val page   = QueryResult(query, Seq[Smile](), 0)
    val result = Future.successful(Right(page))
    when(getSmiles.apply(query)).thenReturn(result)
    asApiModel(page)
  }

  private def givenThereAreSomeSmiles(query: Query): PageApiModel[SmileApiModel] = {
    val smiles     = Gen.listOf(arbitrarySentSmile).one
    val totalCount = Gen.posNum[Long].one
    val page       = QueryResult(query, smiles, totalCount)
    val result     = Future.successful(Right(page))
    when(getSmiles.apply(query)).thenReturn(result)
    asApiModel(page)
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

  private def smileByIdPath(id: String) = {
    s"/api/smile/$id"
  }

}
