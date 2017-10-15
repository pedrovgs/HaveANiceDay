package com.github.pedrovgs.haveaniceday.extensions

import java.util.Date

import org.joda.time.DateTime

object date {
  implicit def asDateTime(date: Date)     = new DateTime(date.getTime)
  implicit def asDate(dateTime: DateTime) = dateTime.toDate
}
