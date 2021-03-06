package com.github.pedrovgs.haveaniceday.smiles.storage

import com.github.pedrovgs.haveaniceday.extensions.sqldate._
import com.github.pedrovgs.haveaniceday.smiles.model.{Smile, SmilesGeneration, SmilesGenerationResult, Source}
import slick.Tables.{SmilesGenerationRow, SmilesRow}

object codec {

  implicit def asRow(smile: Smile): SmilesRow =
    SmilesRow(
      smile.id,
      smile.creationDate,
      smile.photo,
      smile.description,
      smile.source.toString,
      smile.sourceUrl,
      smile.numberOfLikes,
      smile.sent,
      smile.sentDate,
      smile.number
    )

  implicit def asRow(smiles: Seq[Smile]): Seq[SmilesRow] = smiles.map(asRow)

  implicit def asDomain(row: SmilesRow): Smile =
    Smile(
      row.id,
      row.creationDate,
      row.photoUrl,
      row.description,
      Source.withName(row.source),
      row.sourceUrl,
      row.numberOfLikes,
      row.sent,
      row.sentDate,
      row.smileNumber
    )

  implicit def asDomain(rows: Seq[SmilesRow]): Seq[Smile] = rows.map(asDomain)

  implicit def asSmilesGeneration(row: SmilesGenerationRow): SmilesGeneration =
    SmilesGeneration(row.id, row.generationDate, row.smileId, row.error)

  implicit def asSmilesGeneration(rows: Seq[SmilesGenerationRow]): Seq[SmilesGeneration] =
    rows.map(asSmilesGeneration)
}
