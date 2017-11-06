package finatra.api.smiles

import com.github.pedrovgs.haveaniceday.smiles.model.Smile
import com.github.pedrovgs.haveaniceday.smiles.utils.SmileTitleGenerator
import com.github.pedrovgs.haveaniceday.utils.model.QueryResult
import finatra.api.model.PageApiModel

object model {

  case class SmileApiModel(id: Long, title: String, message: Option[String], photoUrl: Option[String])

  implicit def asApiModel(smile: Smile): SmileApiModel =
    SmileApiModel(smile.id,
                  SmileTitleGenerator.generateSmileForTitle(smile.number.getOrElse(1)),
                  smile.description,
                  smile.photo)

  implicit def asApiModel(result: QueryResult[Smile]): PageApiModel[SmileApiModel] =
    PageApiModel(result.data.map(asApiModel), result.totalCount, result.query.page, result.query.pageSize)

}
