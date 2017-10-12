package com.github.pedrovgs.haveaniceday.smiles.storage

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesExtractionResult
import org.joda.time.DateTime

import scala.concurrent.Future

class SmilesExtractorStorage {

  def getLastSmilesExtraction(): Future[Option[DateTime]] = {
    ???
  }

  def updateLastExtractionStorage(date: DateTime, result: SmilesExtractionResult): Future[DateTime] = {
    ???
  }
}
