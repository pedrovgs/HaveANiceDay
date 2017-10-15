package specs

import extensions.futures._
import slick.basic.DatabaseConfig
import slick.jdbc._
import slick.{Database, Tables}

import scala.util.Try

object InMemoryDatabase {
  val tables =
    Seq(Tables.DevelopersTable, Tables.SmilesTable, Tables.SmilesExtractionsTable, Tables.SmilesGenerationTable)
}

trait InMemoryDatabase {

  import InMemoryDatabase._

  lazy val database: Database = {
    val config: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("inMemorySlick")
    val db: JdbcBackend#DatabaseDef         = config.db
    val inMemoryDatabase                    = Database(config, db)
    Try(dropTables(inMemoryDatabase))
    createTables(inMemoryDatabase)
    inMemoryDatabase
  }

  def resetDatabase() = {
    dropTables(database)
    createTables(database)
  }

  private def dropTables(database: Database) = {
    import database.config.profile.api._
    tables.reverse.map(_.schema.drop).foreach { query =>
      database.db.run(query).awaitForResult
    }
  }

  private def createTables(database: Database) = {
    import database.config.profile.api._
    tables.map(_.schema.create).foreach { query =>
      database.db.run(query).awaitForResult
    }
  }
}
