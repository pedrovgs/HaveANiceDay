package com.github.pedrovgs.haveaniceday.smiles.storage

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.storage.codec._
import com.github.pedrovgs.haveaniceday.utils.model
import com.github.pedrovgs.haveaniceday.utils.model.QueryResult
import slick.Database
import slick.Tables.{SmilesRow, SmilesTable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

  def get(query: model.Query): Future[QueryResult[Smile]] = {
    val offset               = (query.page - 1) * query.pageSize
    val pageSize             = query.pageSize
    val dbQuery              = SmilesTable.filter(row => row.sent).sortBy(_.smileNumber.desc).drop(offset).take(pageSize).result
    val countQuery           = SmilesTable.filter(row => row.sent).result
    val eventuallySmilesSent = database.db.run(countQuery)
    val eventuallySmilesPage = database.db.run(dbQuery)
    eventuallySmilesPage.zip(eventuallySmilesSent).map {
      case (smileRows, totalCount) =>
        val smiles: Seq[Smile] = smileRows
        QueryResult(query, smiles, totalCount.length)
    }
  }

  def get(id: Long): Future[Option[Smile]] = {
    val query = SmilesTable.filter(row => row.sent).filter(row => row.id === id).result.headOption
    database.db.run(query).map(_.map(asDomain))
  }

  def getRandomSmile(): Future[Option[Smile]] = {
    val rand  = SimpleFunction.nullary[Double]("rand")
    val query = SmilesTable.filter(row => row.sent).sortBy(_ => rand).take(1).result.headOption
    database.db.run(query).map(_.map(asDomain))
  }

  def update(smile: Smile): Future[Smile] = {
    val query = SmilesTable.filter(row => row.id === smile.id).update(smile)
    database.db.run(query).map(_ => smile)
  }
}
