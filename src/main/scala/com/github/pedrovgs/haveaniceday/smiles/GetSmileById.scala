package com.github.pedrovgs.haveaniceday.smiles

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.smiles.model.GetSmileResult
import com.github.pedrovgs.haveaniceday.smiles.storage.SmilesRepository
import com.github.pedrovgs.haveaniceday.utils.model.ItemNotFound

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GetSmileById @Inject()(repository: SmilesRepository) {

  def apply(id: Long): Future[GetSmileResult] = repository.get(id).map {
    case Some(smile) => Right(smile)
    case None        => Left(ItemNotFound(id.toString))
  }

}
