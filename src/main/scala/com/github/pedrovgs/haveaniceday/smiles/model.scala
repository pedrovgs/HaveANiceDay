package com.github.pedrovgs.haveaniceday.smiles

import akka.http.scaladsl.model.DateTime

object model {

  type SmilesGenerationResult = Either[SmilesGenerationError, Int]

  sealed trait SmilesGenerationError

  case class TryToExtractSmilesTooEarly(date: DateTime)
  case class UnknownError(message: String) extends SmilesGenerationError

  case class SmilesGeneratorConfig(twitterAccounts: Seq[String], numberOfGenerationsPerDay: Int)

  case class Smile()
}
