package com.github.pedrovgs.haveaniceday.smiles

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesExtractionResult

import scala.concurrent.Future

class ExtractSmiles @Inject()(smilesGenerator: SmilesGenerator) {

  def apply(): Future[SmilesExtractionResult] = smilesGenerator.extractSmiles()

}
