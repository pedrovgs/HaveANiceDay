package com.github.pedrovgs.haveaniceday.smiles

import org.joda.time.DateTime

object model {

  type SmilesExtractionResult = Either[SmilesExtractionError, Seq[Smile]]

  sealed trait SmilesExtractionError {
    val message: String

    override def toString: String = message
  }

  case class TryToExtractSmilesTooEarly(date: DateTime) extends SmilesExtractionError {
    override val message: String =
      s"Try to extract smiles too soon. Extraction date $date. Review your cron jobs configuration."
  }

  case class UnknownError(message: String) extends SmilesExtractionError

  case class SmilesGeneratorConfig(twitterAccounts: List[String], numberOfExtractionsPerDay: Int)

  object Source extends Enumeration {
    val Twitter = Value
  }

  case class Smile(id: Long,
                   creationDate: DateTime,
                   photo: Option[String],
                   description: Option[String],
                   source: Source.Value,
                   sourceUrl: String,
                   sent: Boolean,
                   sentDate: Option[DateTime],
                   number: Option[Int])

}
