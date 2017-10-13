package com.github.pedrovgs.haveaniceday.smiles.apiclient

import com.danielasfregola.twitter4s.TwitterRestClient
import com.github.pedrovgs.haveaniceday.smiles.model.SmilesExtractionResult
import org.joda.time.DateTime
import scala.concurrent.Future

class TwitterClient {

  def smilesFrom(extractionDate: DateTime): Future[SmilesExtractionResult] = ???

  private val restClient = TwitterRestClient()

}
