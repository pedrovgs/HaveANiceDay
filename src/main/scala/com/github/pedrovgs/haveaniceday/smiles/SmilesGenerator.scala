package com.github.pedrovgs.haveaniceday.smiles

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.notifications.client.NotificationsClient
import com.github.pedrovgs.haveaniceday.notifications.model.Notification
import com.github.pedrovgs.haveaniceday.smiles.apiclient.TwitterClient
import com.github.pedrovgs.haveaniceday.smiles.model._
import com.github.pedrovgs.haveaniceday.smiles.storage.{SmilesExtractionsRepository, SmilesRepository}
import com.github.pedrovgs.haveaniceday.utils.Clock
import com.twitter.inject.Logging
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
                                notificationsClient: NotificationsClient,
                                clock: Clock)
    extends Logging {

  import SmilesGenerator._

  def extractSmiles(): Future[SmilesExtractionResult] =
    for {
      lastExtractionDate <- smilesExtractorRepository.getLastSmilesExtraction
      result <- if (shouldExtractSmiles(lastExtractionDate)) {
        extractSmilesFromTwitterSince(lastExtractionDate)
      } else {
        tooEarlySmilesExtraction(clock.now)
      }
    } yield result

  def generateSmiles(): Future[SmilesGenerationResult] = {
    smilesRepository.getNextMostRatedNotSentSmile().flatMap {
      case Some(smile) => sendSmileAndMarkItAsSent(smile)
      case None        => Future.successful(Left(NoExtractedSmilesFound))
    }
  }

  private def sendSmileAndMarkItAsSent(smile: Smile): Future[SmilesGenerationResult] = {
    for {
      sendSmileResult <- sendSmile(smile)
      _ <- sendSmileResult match {
        case Right(smileMarkedAsSent) =>
          info(s"Smile sent properly $smileMarkedAsSent")
          smilesRepository.update(smileMarkedAsSent)
        case Left(sendSmileError) =>
          error(s"Error found while sending a smile ${sendSmileError.message}")
          Future.successful(None)
      }
    } yield sendSmileResult
  }

  private def sendSmile(smile: Smile): Future[SmilesGenerationResult] = {
    val title               = "Have a nice day 😃"
    val message             = smile.description.getOrElse(title)
    val photoUrl            = smile.photo
    val notification        = Notification(title, message, photoUrl)
    val lastSmileSentFuture = smilesRepository.getLastSmileSent()
    lastSmileSentFuture.zip(notificationsClient.sendNotificationToEveryUser(notification)).map {
      case (lastSmileSent, Right(_)) =>
        val smileNumber: Int = lastSmileSent.flatMap(_.number.map(_ + 1)).getOrElse(1)
        smile.copy(sent = true, sentDate = Some(clock.now), number = Some(smileNumber))
        Right(smile)
      case (_, Left(error)) =>
        Left(UnknownError(
          s"Something went wrong while sending the notification. Error code: ${error.code} Error message: ${error.message}"))
    }
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
