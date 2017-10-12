package com.github.pedrovgs.haveaniceday.smiles.storage

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGenerationResult
import com.twitter.util.Future
import org.joda.time.DateTime

class SmilesExtractorStorage {

  def getLastSmilesExtraction(): Future[DateTime] = {
    ???
  }

  def updateLastExtractionStorage(date: DateTime, result: SmilesGenerationResult): Future[DateTime] = {
    ???
  }
}
