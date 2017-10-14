package com.github.pedrovgs.haveaniceday.smiles

import org.joda.time.DateTime

object model {

  type SmilesExtractionResult = Either[SmilesExtractionError, Seq[Smile]]
  type SmilesGenerationResult = Either[SmilesGenerationError, Smile]

  sealed trait SmilesExtractionError {
    val message: String

    override def toString: String = message
  }

  sealed trait SmilesGenerationError {
    val message: String

    override def toString: String = message
  }

  case object NoExtractedSmilesFound extends SmilesGenerationError {
    override val message: String =
      s"Try to extract generate smiles but there are no smiles generated previously."
  }

  case class UnknownError(message: String) extends SmilesExtractionError with SmilesGenerationError

  case class SmilesGeneratorConfig(twitterAccounts: List[String],
                                   scheduleTasks: Boolean,
                                   extractionSchedule: String,
                                   generationSchedule: String)

  object Source extends Enumeration {
    val Twitter = Value
  }

  case class Smile(id: Long,
                   creationDate: DateTime,
                   photo: Option[String],
                   description: Option[String],
                   source: Source.Value,
                   sourceUrl: String,
                   numberOfLikes: Long,
                   sent: Boolean,
                   sentDate: Option[DateTime],
                   number: Option[Int])

}
