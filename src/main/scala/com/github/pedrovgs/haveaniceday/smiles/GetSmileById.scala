package com.github.pedrovgs.haveaniceday.smiles

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.smiles.model.GetSmileResult
import com.github.pedrovgs.haveaniceday.smiles.storage.SmilesRepository

import scala.concurrent.Future

class GetSmileById @Inject()(repository: SmilesRepository) {

  def apply(id: Long): Future[GetSmileResult] = repository.get(id)

}
