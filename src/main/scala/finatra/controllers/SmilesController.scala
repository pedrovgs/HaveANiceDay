package finatra.controllers

import com.github.pedrovgs.haveaniceday.smiles.{GetRandomSmile, GetSmileById, GetSmiles}
import finatra.utils.QueryRequestExtractor._
import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.Logging
import finatra.api.model.PageApiModel
import finatra.api.smiles.model._

import scala.concurrent.ExecutionContext.Implicits.global

class SmilesController @Inject()(getSmiles: GetSmiles, getSmileById: GetSmileById, getRandomSmile: GetRandomSmile)
    extends Controller
    with Logging {

  get("/api/smiles") { request: Request =>
    withQuery(request, response) { query =>
      getSmiles(query).map {
        case Right(smiles) =>
          val responsePage: PageApiModel[SmileApiModel] = smiles
          responsePage
        case Left(error) =>
          response.badRequest(error.message)
      }
    }
  }

  get("/api/smile/:id") { request: Request =>
    withId(request, response) { id =>
      getSmileById(id).map {
        case Right(smile) =>
          val smileApiModel: SmileApiModel = smile
          smileApiModel
        case Left(error) => response.notFound.body(error.message)
      }
    }
  }

  get("/api/smile") { _: Request =>
    getRandomSmile().map {
      case Right(smile) =>
        val smileApiModel: SmileApiModel = smile
        smileApiModel
      case Left(error) => response.notFound.body(error.message)
    }
  }
}
