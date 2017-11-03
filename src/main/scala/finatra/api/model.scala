package finatra.api

object model {

  case class PageApiModel[T](data: Seq[T], totalCount: Long, page: Long, pageSize: Int)

}
