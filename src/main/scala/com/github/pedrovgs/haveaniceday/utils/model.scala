package com.github.pedrovgs.haveaniceday.utils

object model {

  case class Query(page: Int, pageSize: Int = 25)
  case class QueryResult[T](query: Query, data: Seq[T], totalCount: Int)

  sealed trait HaveANiceDayError
  case class InvalidQuery(message: String)
  case class ItemNotFound() extends HaveANiceDayError

}
