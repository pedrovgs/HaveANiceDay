package com.github.pedrovgs.haveaniceday.smiles

import org.joda.time.DateTime

object model {

  type SmilesExtractionResult = Either[SmilesExtractionError, Int]

  sealed trait SmilesExtractionError

  case class TryToExtractSmilesTooEarly(date: DateTime) extends SmilesExtractionError
  case class UnknownError(message: String)              extends SmilesExtractionError

  case class SmilesGeneratorConfig(twitterAccounts: Seq[String], numberOfExtractionsPerDay: Int)

  case class Smile()
}
