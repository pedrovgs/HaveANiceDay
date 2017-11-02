package com.github.pedrovgs.haveaniceday.smiles

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.smiles.model.SmilesGenerationResult

import scala.concurrent.Future

class GenerateSmiles @Inject()(smilesGenerator: SmilesGenerator) {

  def apply(): Future[SmilesGenerationResult] = smilesGenerator.generateSmiles()

}
