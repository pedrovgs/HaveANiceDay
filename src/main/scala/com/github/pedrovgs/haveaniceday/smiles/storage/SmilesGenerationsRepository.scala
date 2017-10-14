package com.github.pedrovgs.haveaniceday.smiles.storage

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.extensions.sqldate._
import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGenerationResult
import com.github.pedrovgs.haveaniceday.utils.Clock
import slick.Database
import slick.Tables.{SmilesGenerationRow, SmilesGenerationTable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SmilesGenerationsRepository @Inject()(database: Database, clock: Clock) {

  import database.config.profile.api._

  def saveLastGenerationStorage(result: SmilesGenerationResult): Future[SmilesGenerationResult] = {
    val row = result match {
      case Right(smile) => SmilesGenerationRow(0, clock.now, Some(smile.id), None)
      case Left(error)  => SmilesGenerationRow(0, clock.now, None, Some(error.message))
    }
    val insertQuery = SmilesGenerationTable returning SmilesGenerationTable
      .map(_.id) into ((row, id) => row.copy(id = id))
    database.db.run(insertQuery += row).map(_ => result)
  }
}
