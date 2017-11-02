package finatra.api

object model {

  case class PaginatedResponse[T](data: Seq[T], totalCount: Long, page: Long, pageSize: Int)

}
