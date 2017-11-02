package com.github.pedrovgs.haveaniceday.utils

import com.github.pedrovgs.haveaniceday.utils.model.Query
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.util.Try

import scala.concurrent.Future

object QueryRequestExtractor {

  def withQuery(request: Request, responseBuilder: ResponseBuilder)(f: (Query => Future[Any])): Future[Any] = {
    val page     = Try(request.params("page").toLong).toOption.getOrElse(1L)
    val pageSize = Try(request.params("pageSize").toInt).toOption.getOrElse(25)
    if (page <= 0)
      Future.successful(
        responseBuilder.badRequest(
          "The page passed as parameter possible values are between 1 and the max Long value"))
    else if (pageSize <= 0) Future.successful(responseBuilder.badRequest("The page size can't be negative or zero"))
    else if (pageSize > 100) Future.successful(responseBuilder.badRequest("The page size can't be greater than 100"))
    else f(Query(page, pageSize))
  }

  def withId(request: Request, responseBuilder: ResponseBuilder)(f: (Long => Future[Any])): Future[Any] = {
    Try(request.params("id").toLong).toOption match {
      case Some(id) => f(id)
      case None     => Future.successful(responseBuilder.badRequest("There is a missing id param which is mandatory."))
    }

  }
}
