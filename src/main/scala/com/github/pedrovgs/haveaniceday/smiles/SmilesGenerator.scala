package com.github.pedrovgs.haveaniceday.smiles

import java.util.concurrent.TimeUnit

import com.github.pedrovgs.haveaniceday.smiles.apiclient.TwitterClient
import com.github.pedrovgs.haveaniceday.smiles.model.{
  SmilesExtractionResult,
  SmilesGeneratorConfig,
  TryToExtractSmilesTooEarly
}
import com.github.pedrovgs.haveaniceday.smiles.storage.SmilesExtractorStorage
import com.github.pedrovgs.haveaniceday.utils.Clock
import org.joda.time
import org.joda.time.DateTime
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object SmilesGenerator {
  private def tooEarlySmilesExtraction(dateTime: DateTime): Future[SmilesExtractionResult] = {
    val error = Left(TryToExtractSmilesTooEarly(dateTime))
    Future.successful(error)
  }
}

class SmilesGenerator(config: SmilesGeneratorConfig,
                      twitterClient: TwitterClient,
                      smilesExtractorStorage: SmilesExtractorStorage,
                      clock: Clock) {

  import SmilesGenerator._

  def extractSmiles(): Future[SmilesExtractionResult] = {
    for {
      lastExtractionDate <- smilesExtractorStorage.getLastSmilesExtraction()
      result <- if (shouldExtractSmiles(lastExtractionDate)) {
        extractSmilesFromTwitterSince(lastExtractionDate)
      } else {
        tooEarlySmilesExtraction(clock.now)
      }
    } yield result
  }

  private def shouldExtractSmiles(lastExtractionDate: Option[DateTime]): Boolean = {
    val numberOfTriesPerDay           = config.numberOfExtractionsPerDay
    val minimumDifferenceInHours: Int = 24 / numberOfTriesPerDay
    val now                           = clock.now

    def hasElapsedTheMinimumAmountOfTimeSinceTheLastExtraction(lastExtractionDate: time.DateTime) = {
      TimeUnit.MILLISECONDS.toHours(now.getMillis - lastExtractionDate.getMillis) >= minimumDifferenceInHours
    }

    lastExtractionDate match {
      case Some(date) => hasElapsedTheMinimumAmountOfTimeSinceTheLastExtraction(date)
      case _          => false
    }
  }

  private def extractSmilesFromTwitterSince(date: Option[DateTime]): Future[SmilesExtractionResult] = {
    val extractionDate = date.getOrElse(clock.now.minusMonths(1))
    ???
  }

}
