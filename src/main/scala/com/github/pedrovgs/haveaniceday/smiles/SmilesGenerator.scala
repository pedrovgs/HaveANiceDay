package com.github.pedrovgs.haveaniceday.smiles

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import com.twitter.inject.Logging
import com.github.pedrovgs.haveaniceday.smiles.apiclient.TwitterClient
import com.github.pedrovgs.haveaniceday.smiles.model.{
  SmilesExtractionResult,
  SmilesGenerationResult,
  SmilesGeneratorConfig,
  TryToExtractSmilesTooEarly
}
import com.github.pedrovgs.haveaniceday.smiles.storage.{SmilesExtractionsRepository, SmilesRepository}
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

class SmilesGenerator @Inject()(config: SmilesGeneratorConfig,
                                twitterClient: TwitterClient,
                                smilesExtractorRepository: SmilesExtractionsRepository,
                                smilesRepository: SmilesRepository,
                                clock: Clock)
    extends Logging {

  import SmilesGenerator._

  def extractSmiles(): Future[SmilesExtractionResult] = {
    for {
      lastExtractionDate <- smilesExtractorRepository.getLastSmilesExtraction
      result <- if (shouldExtractSmiles(lastExtractionDate)) {
        extractSmilesFromTwitterSince(lastExtractionDate)
      } else {
        tooEarlySmilesExtraction(clock.now)
      }
    } yield result
  }

  def generateSmiles(): Future[SmilesGenerationResult] = {
    ???
  }

  private def shouldExtractSmiles(lastExtractionDate: Option[DateTime]): Boolean = {
    val numberOfTriesPerDay           = config.numberOfExtractionsPerDay
    val minimumDifferenceInHours: Int = 24 / numberOfTriesPerDay
    val now                           = clock.now

    def hasElapsedTheMinimumAmountOfTimeSinceTheLastExtraction(lastExtractionDate: DateTime) = {
      TimeUnit.MILLISECONDS.toHours(now.getMillis - lastExtractionDate.getMillis) >= minimumDifferenceInHours
    }

    lastExtractionDate match {
      case Some(date) => hasElapsedTheMinimumAmountOfTimeSinceTheLastExtraction(date)
      case _          => true
    }
  }

  private def extractSmilesFromTwitterSince(date: Option[DateTime]): Future[SmilesExtractionResult] = {
    val extractionDate = date.getOrElse(clock.now.minusMonths(1))
    val accounts       = config.twitterAccounts
    twitterClient.smilesFrom(accounts, extractionDate).flatMap {
      case Right(smiles) =>
        for {
          savedSmiles <- smilesRepository.saveSmiles(smiles)
          _           <- smilesExtractorRepository.updateLastExtractionStorage(clock.now, savedSmiles.length)
        } yield Right(savedSmiles)
      case Left(extractionError) =>
        error(s"Error extracting smiles from twitter: $extractionError")
        Future.successful(Left(extractionError))
    }
  }

}
