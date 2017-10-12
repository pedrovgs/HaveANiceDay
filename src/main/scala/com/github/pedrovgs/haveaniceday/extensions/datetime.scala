package com.github.pedrovgs.haveaniceday.extensions

import java.sql.{Date, Timestamp}

import org.joda.time.DateTime

object datetime {

  implicit def dateTimeToTimestamp(dateTime: DateTime): Timestamp =
    new Timestamp(dateTime.toDate.getTime)

  implicit def optionDateTimeToOptionTimestamp(dateTime: Option[DateTime]): Option[Timestamp] =
    dateTime.map(dateTimeToTimestamp)

  implicit def dateToDateTime(date: Date): DateTime = new DateTime(date.getTime)

  implicit def timestampToDateTime(timestamp: Timestamp): DateTime =
    new DateTime(timestamp.getTime)

  implicit def optionTimestampToDateTim(timestamp: Option[Timestamp]): Option[DateTime] =
    timestamp.map(timestampToDateTime)
}
