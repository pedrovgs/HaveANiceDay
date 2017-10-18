package com.github.pedrovgs.haveaniceday.smiles

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.notifications.client.NotificationsClient
import com.github.pedrovgs.haveaniceday.smiles.apiclient.TwitterClient
import com.github.pedrovgs.haveaniceday.smiles.model._
import com.github.pedrovgs.haveaniceday.smiles.storage.{
  SmilesExtractionsRepository,
  SmilesGenerationsRepository,
  SmilesRepository
}
import com.github.pedrovgs.haveaniceday.utils.Clock
import com.twitter.inject.Logging
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SmilesGenerator @Inject()(config: SmilesGeneratorConfig,
                                twitterClient: TwitterClient,
                                smilesExtractorRepository: SmilesExtractionsRepository,
                                smilesGenerationRepository: SmilesGenerationsRepository,
                                smilesRepository: SmilesRepository,
                                notificationsClient: NotificationsClient,
                                clock: Clock)
    extends Logging {

  def extractSmiles(): Future[SmilesExtractionResult] =
    for {
      lastExtractionDate <- smilesExtractorRepository.getLastSmilesExtraction
      result             <- extractSmilesFromTwitterSince(lastExtractionDate)
    } yield result

  def generateSmiles(): Future[SmilesGenerationResult] = {
    for {
      result <- smilesRepository.getNextMostRatedNotSentSmile().flatMap {
        case Some(smile) => sendSmileAndMarkItAsSent(smile)
        case None        => Future.successful(Left(NoExtractedSmilesFound))
      }
      _ <- smilesGenerationRepository.saveLastGenerationStorage(result)
    } yield result

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
    for {
      lastSmileSent <- smilesRepository.getLastSmileSent()
      smileNumber = lastSmileSent.flatMap(_.number.map(_ + 1)).getOrElse(1)
      sendNotificationResult <- notificationsClient.sendSmileToEveryUser(smile, smileNumber)
    } yield
      sendNotificationResult match {
        case Right(_) =>
          val smileMarkedAsSent = smile.copy(sent = true, sentDate = Some(clock.now), number = Some(smileNumber))
          Right(smileMarkedAsSent)
        case Left(error) =>
          Left(UnknownError(s"Something went wrong while sending the notification. Error message: ${error.message}"))
      }
  }

  private def extractSmilesFromTwitterSince(date: Option[DateTime]): Future[SmilesExtractionResult] = {
    val extractionDate = date.getOrElse(clock.now.minusMonths(1))
    val accounts       = config.twitterAccounts
    twitterClient.smilesFrom(accounts, extractionDate).flatMap {
      case Right(smiles) =>
        for {
          savedSmiles <- smilesRepository.saveSmiles(smiles)
          _           <- smilesExtractorRepository.saveLastExtractionStorage(clock.now, savedSmiles.length)
        } yield Right(savedSmiles)
      case Left(extractionError) =>
        error(s"Error extracting smiles from twitter: $extractionError")
        Future.successful(Left(extractionError))
    }
  }

}
