package com.github.pedrovgs.haveaniceday.smiles

import org.joda.time.DateTime

object model {

  type SmilesExtractionResult = Either[SmilesExtractionError, Int]

  sealed trait SmilesExtractionError

  case class TryToExtractSmilesTooEarly(date: DateTime) extends SmilesExtractionError

  case class UnknownError(message: String) extends SmilesExtractionError

  case class SmilesGeneratorConfig(twitterAccounts: List[String], numberOfExtractionsPerDay: Int)

  object Source extends Enumeration {
    val Twitter = Value
  }

  case class Smile(id: Int,
                   creationDate: DateTime,
                   photo: Option[String],
                   description: Option[String],
                   source: Source.Value,
                   sourceUrl: String,
                   sent: Boolean,
                   sentDate: Option[DateTime],
                   smileNumber: Option[Int])

}
