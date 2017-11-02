package finatra.controllers

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.{GetRandomSmile, GetSmileById, GetSmiles}
import com.github.pedrovgs.haveaniceday.utils.QueryRequestExtractor._
import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import finatra.api.model.PaginatedResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

class SmilesController @Inject()(getSmiles: GetSmiles, getSmileById: GetSmileById, getRandomSmile: GetRandomSmile)
    extends Controller
    with Logging {

  get("/api/smiles") { request: Request =>
    query(request, response) { query =>
      getSmiles(query).map {
        case Right(smiles) =>
          val responsePage: PaginatedResponse[Smile] = smiles
          response.ok.body(responsePage)
        case Left(error) =>
          response.badRequest(error.message)
      }
    }
  }

  get("/api/smile/:id") { request: Request =>
    Try(request.params("id").toLong).toOption match {
      case Some(id) => getSmileById(id)
      case None     => Future.successful(response.notFound)
    }
  }

  get("/api/randomSmile") { _: Request =>
    getRandomSmile().map { smile =>
      response.ok.body(smile)
    }
  }
}
