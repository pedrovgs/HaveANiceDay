package com.github.pedrovgs.haveaniceday.utils

import com.github.pedrovgs.haveaniceday.utils.model.{HaveANiceDayError, InvalidQuery, Query}

object QueryValidator {
  def isValid(query: Query): Option[HaveANiceDayError] = {
    if (query.page <= 0)
      Some(InvalidQuery("The page passed as parameter possible values are between 1 and the max Long value"))
    else if (query.pageSize <= 0)
      Some(InvalidQuery("The page size can't be negative or zero"))
    else if (query.pageSize > 100)
      Some(InvalidQuery("The page size can't be greater than 100"))
    else None
  }
}
