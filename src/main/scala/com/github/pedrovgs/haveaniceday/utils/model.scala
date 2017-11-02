package com.github.pedrovgs.haveaniceday.utils

object model {

  case class Query(page: Int, pageSize: Int = 25)

  case class QueryResult[T](query: Query, data: Seq[T], totalCount: Int)

  sealed trait HaveANiceDayError {
    val message: String
  }

  case class InvalidQuery(message: String)

  case class ItemNotFound(id: String) extends HaveANiceDayError {
    val message: String = s"Item with id $id not found"
  }

}