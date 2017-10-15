package com.github.pedrovgs.haveaniceday.smiles.storage

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.smiles.model.{Smile, SmilesGenerationResult}
import com.github.pedrovgs.haveaniceday.smiles.storage.codec._
import slick.Database
import slick.Tables.{SmilesRow, SmilesTable}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SmilesRepository @Inject()(database: Database) {

  import database.config.profile.api._

  def saveSmiles(smiles: Seq[Smile]): Future[Seq[Smile]] = {
    val smileRows: Seq[SmilesRow] = smiles
    val insertQuery = SmilesTable returning SmilesTable
      .map(_.id) into ((row, id) => row.copy(id = id))
    val inserts = smileRows.map { row =>
      insertQuery += row
    }
    database.db.run(DBIO.sequence(inserts).transactionally).map(asDomain)
  }

  def getNextMostRatedNotSentSmile(): Future[Option[Smile]] = {
    val query =
      SmilesTable.filterNot(_.sent).sortBy(row => (row.numberOfLikes.desc, row.id.desc)).take(1).result.headOption
    database.db.run(query).map {
      case Some(row) => Some(row)
      case _         => None
    }
  }

  def getLastSmileSent(): Future[Option[Smile]] = {
    val query = SmilesTable.filter(row => row.sent).sortBy(_.smileNumber.desc).take(1).result.headOption
    database.db.run(query).map {
      case Some(row) => Some(row)
      case _         => None
    }
  }

  def update(smile: Smile): Future[Smile] = {
    val query = SmilesTable.filter(row => row.id === smile.id).update(smile)
    database.db.run(query).map(_ => smile)
  }
}
