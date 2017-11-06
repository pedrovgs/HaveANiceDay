package slick

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.inject.{Logging, TwitterModule}
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

object SlickModule extends TwitterModule with Logging {

  @Singleton
  @Provides
  def database: Database = {
    val config: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("slick")
    val db: JdbcBackend#DatabaseDef         = config.db
    info(s"Providing DB connection for: $config")
    Database(config, db)
  }

}

case class Database(config: DatabaseConfig[JdbcProfile], db: JdbcBackend#DatabaseDef)
