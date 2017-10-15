package slick

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

object SlickModule extends TwitterModule {

  @Provides
  def database: Database = {
    val config: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("slick")
    val db: JdbcBackend#DatabaseDef         = config.db
    Database(config, db)
  }

}

case class Database(config: DatabaseConfig[JdbcProfile], db: JdbcBackend#DatabaseDef)
