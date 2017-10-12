package com.github.pedrovgs.haveaniceday.smiles.storage

import com.github.pedrovgs.haveaniceday.extensions.datetime._
import org.joda.time.DateTime
import slick.Database
import slick.Tables.{SmilesExtractionsRow, SmilesExtractionsTable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SmilesExtractionsRepository(database: Database) {

  import database.config.profile.api._

  def getLastSmilesExtraction: Future[Option[DateTime]] = {
    val lastExtractionQuery = SmilesExtractionsTable.sortBy(_.extractionDate.desc).take(1).result.headOption
    database.db.run(lastExtractionQuery).map {
      case Some(row) => Some(row.extractionDate)
      case _         => None
    }
  }

  def updateLastExtractionStorage(date: DateTime, smilesExtractedCount: Int): Future[DateTime] = {
    val row = SmilesExtractionsRow(0, date, smilesExtractedCount)
    val insertQuery = SmilesExtractionsTable returning SmilesExtractionsTable
      .map(_.id) into ((row, id) => row.copy(id = id))
    database.db.run(insertQuery += row).map(_.extractionDate)
  }
}
