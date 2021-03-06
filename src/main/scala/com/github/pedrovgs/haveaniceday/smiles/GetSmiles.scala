package com.github.pedrovgs.haveaniceday.smiles

import javax.inject.Inject

import com.github.pedrovgs.haveaniceday.smiles.model.GetSmilesResult
import com.github.pedrovgs.haveaniceday.smiles.storage.SmilesRepository
import com.github.pedrovgs.haveaniceday.utils.QueryValidator._
import com.github.pedrovgs.haveaniceday.utils.model.Query

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GetSmiles @Inject()(repository: SmilesRepository) {

  def apply(query: Query): Future[GetSmilesResult] = {
    isValid(query) match {
      case Some(error) => Future.successful(Left(error))
      case None        => repository.get(query).map(Right(_))
    }
  }

}
