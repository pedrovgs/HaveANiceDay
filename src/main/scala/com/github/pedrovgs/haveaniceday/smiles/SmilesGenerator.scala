package com.github.pedrovgs.haveaniceday.smiles

import com.github.pedrovgs.haveaniceday.smiles.apiclient.TwitterClient
import com.github.pedrovgs.haveaniceday.smiles.model.{
  SmilesGenerationResult,
  SmilesGeneratorConfig,
  TryToExtractSmilesTooEarly
}
import com.github.pedrovgs.haveaniceday.utils.Clock

import scala.concurrent.Future

class SmilesGenerator(config: SmilesGeneratorConfig, twitterClient: TwitterClient, clock: Clock) {

  def extractSmiles(): Future[SmilesGenerationResult] = {
    for {
      shouldExtractSmiles <- shouldExtractSmiles()
      result <- if (shouldExtractSmiles) {
        extractSmilesFromTwitter()
      } else {
        val error = Left(TryToExtractSmilesTooEarly(clock.now))
        Future.successful(error)
      }
    } yield result
  }

  private def shouldExtractSmiles(): Future[Boolean] = {
    ???
  }

  private def extractSmilesFromTwitter(): Future[SmilesGenerationResult] = {
    ???
  }

}
