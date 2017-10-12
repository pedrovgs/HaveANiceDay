package specs

import slick.basic.DatabaseConfig
import slick.jdbc._
import slick.{Database, Tables}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try
object InMemoryDatabase {
  val tables = Seq(Tables.DevelopersTable, Tables.SmilesExtractionsTable)
}
trait InMemoryDatabase {
  import InMemoryDatabase._

  lazy val database: Database = {
    val config: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("inMemorySlick")
    val db: JdbcBackend#DatabaseDef         = config.db
    Database(config, db)
  }

  def resetDatabase(): Database = {
    Try(dropTables(database))
    createTables(database)
    database
  }

  private def dropTables(database: Database) = {
    import database.config.profile.api._
    val dropTablesQuery = DBIO.seq(tables.reverseMap(_.schema.drop): _*)
    Await.result(database.db.run(dropTablesQuery), Duration.Inf)
  }

  private def createTables(database: Database) = {
    import database.config.profile.api._
    val createTablesQuery = DBIO.seq(tables.map(_.schema.create): _*)
    Await.result(database.db.run(createTablesQuery), Duration.Inf)
  }
}
