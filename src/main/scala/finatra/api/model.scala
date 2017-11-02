package finatra.api

import com.github.pedrovgs.haveaniceday.utils.model.QueryResult

object model {

  case class PaginatedResponse[T](data: Seq[T], totalCount: Long, page: Int, pageSize: Int)

  implicit def asPaginatedResponse[T](queryResult: QueryResult[T]): PaginatedResponse[T] =
    PaginatedResponse(queryResult.data, queryResult.totalCount, queryResult.query.page, queryResult.query.pageSize)

}
