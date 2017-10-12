package specs

import slick.basic.DatabaseConfig
import slick.jdbc._
import slick.{Database, Tables}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

trait InMemoryDatabase {

  lazy val database: Database = {
    val config: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("inMemorySlick")
    val db: JdbcBackend#DatabaseDef = config.db
    val database = Database(config, db)
    resetDatabase(database)
  }

  private def resetDatabase(database: Database): Database = {
    Try(dropTables(database))
    createTables(database)
    database
  }

  private def dropTables(database: Database) = {
    import database.config.profile.api._
    Await.result(database.db.run(DBIO.seq(
      Tables.DevelopersTable.schema.drop,
      Tables.SmilesExtractionsTable.schema.drop))
      , Duration.Inf
    )
  }
  private def createTables(database: Database) = {
    import database.config.profile.api._
    Await.result(database.db.run(DBIO.seq(
      Tables.DevelopersTable.schema.create,
      Tables.SmilesExtractionsTable.schema.create))
      , Duration.Inf
    )
  }
}
