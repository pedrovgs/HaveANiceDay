package specs

import slick.basic.DatabaseConfig
import slick.jdbc._
import slick.{Database, Tables}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

object InMemoryDatabase {
  val tables = Seq(Tables.DevelopersTable, Tables.SmilesExtractionsTable, Tables.SmilesTable)
}

trait InMemoryDatabase {

  import InMemoryDatabase._

  lazy val database: Database = {
    val config: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("inMemorySlick")
    val db: JdbcBackend#DatabaseDef         = config.db
    Database(config, db)
  }

  def resetDatabase(): Database = {
    dropTables(database)
    createTables(database)
    database
  }

  private def dropTables(database: Database) = {
    import database.config.profile.api._
    tables.map(_.schema.drop).foreach { query =>
      Try(Await.result(database.db.run(query), Duration.Inf))
    }
  }

  private def createTables(database: Database) = {
    import database.config.profile.api._
    tables.map(_.schema.create).foreach { query =>
      Try(Await.result(database.db.run(query), Duration.Inf))
    }
  }
}
