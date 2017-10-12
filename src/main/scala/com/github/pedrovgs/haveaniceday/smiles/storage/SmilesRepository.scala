package com.github.pedrovgs.haveaniceday.smiles.storage

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.storage.codec._
import slick.Database
import slick.Tables.{SmilesRow, SmilesTable}

import scala.concurrent.Future

class SmilesRepository(database: Database) {
  import database.config.profile.api._

  def saveSmiles(smiles: Seq[Smile]): Future[Seq[Smile]] = {
    val smileRows: Seq[SmilesRow] = smiles
    val insertQuery = SmilesTable returning SmilesTable
      .map(_.id) into ((row, id) => row.copy(id = id))
    val inserts = smileRows.map { row =>
      insertQuery += row
    }
    database.db.run(DBIO.sequence(inserts)).map(asDomain)
  }
}
