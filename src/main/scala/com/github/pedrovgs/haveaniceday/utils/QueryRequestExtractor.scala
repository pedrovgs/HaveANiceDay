package com.github.pedrovgs.haveaniceday.utils

import com.github.pedrovgs.haveaniceday.utils.model.Query
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.util.Try

import scala.concurrent.Future

object QueryRequestExtractor {

  def query(request: Request, responseBuilder: ResponseBuilder)(
      f: (Query => Future[ResponseBuilder#EnrichedResponse])) = {
    val page     = Try(request.params("page").toLong).toOption
    val pageSize = Try(request.params("pageSize").toInt).toOption
    (page, pageSize) match {
      case (None, _) => responseBuilder.badRequest("The page passed as parameter has to be an Long")
      case (_, None) => responseBuilder.badRequest("Te pageSize passed as parameter has to be an Integer")
      case (Some(pageValue), Some(pageSizeValue)) =>
        if (pageValue <= 0)
          responseBuilder.badRequest(
            "The page passed as parameter possible values are between 0 and the max Long value")
        else if (pageSizeValue <= 0) responseBuilder.badRequest("The page size can't be negative or zero")
        else if (pageSizeValue > 100) responseBuilder.badRequest("The page size can't be greater than 100")
    }
  }
}
