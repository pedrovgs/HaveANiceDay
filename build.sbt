name := "HaveANiceDay"
version := Versions.project
scalaVersion := Versions.scala

mainClass in(Compile, run) := Some("finatra.HaveANiceDayServerMain")

enablePlugins(ScalafmtPlugin)
CommandAliases.addCommandAliases()

libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra
libraryDependencies += "ch.qos.logback" % "logback-classic" % Versions.logback
libraryDependencies += "com.jakehschwartz" %% "finatra-swagger" % Versions.finatraSwagger
libraryDependencies += "com.h2database" % "h2" % Versions.flyway
libraryDependencies += "mysql" % "mysql-connector-java" % Versions.mysqlConnector
libraryDependencies += "com.47deg" %% "classy-core" % Versions.caseClassy
libraryDependencies += "com.47deg" %% "classy-config-typesafe" % Versions.caseClassy
libraryDependencies += "com.47deg" %% "classy-generic" % Versions.caseClassy
libraryDependencies += "org.scalaj" %% "scalaj-http" % Versions.scalajHttp
libraryDependencies += "io.circe" %% "circe-core" % Versions.circe
libraryDependencies += "io.circe" %% "circe-generic" % Versions.circe
libraryDependencies += "io.circe" %% "circe-parser" % Versions.circe
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % Versions.slick,
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % Versions.slick
)
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % Versions.slick
libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra % Test classifier "tests"
libraryDependencies += "ch.qos.logback" % "logback-classic" % Versions.logback % Test
libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra % Test
libraryDependencies += "com.twitter" %% "inject-server" % Versions.finatra % Test
libraryDependencies += "com.twitter" %% "inject-app" % Versions.finatra % Test
libraryDependencies += "com.twitter" %% "inject-core" % Versions.finatra % Test
libraryDependencies += "com.twitter" %% "inject-modules" % Versions.finatra % Test
libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra % Test classifier "tests"
libraryDependencies += "com.twitter" %% "inject-server" % Versions.finatra % Test classifier "tests"
libraryDependencies += "com.twitter" %% "inject-app" % Versions.finatra % Test classifier "tests"
libraryDependencies += "com.twitter" %% "inject-core" % Versions.finatra % Test classifier "tests"
libraryDependencies += "com.twitter" %% "inject-modules" % Versions.finatra % Test classifier "tests"
libraryDependencies += "com.google.inject.extensions" % "guice-testlib" % Versions.guice % Test
libraryDependencies += "org.mockito" % "mockito-core" % Versions.mockito % Test
libraryDependencies += "org.scalatest" %% "scalatest" % Versions.scalatest % Test
libraryDependencies += "com.github.tomakehurst" % "wiremock" % Versions.wiremock % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % Versions.scalacheck % Test

coverageEnabled := true

val dbUrl = "jdbc:mysql://localhost/haveaniceday?characterEncoding=UTF-8&nullNamePatternMatchesAll=true"
val dbUser = "haveaniceday"
val dbPass = "haveaniceday"

flywayUrl := dbUrl
flywayUser := dbUser
flywayPassword := dbPass

import slick.codegen.SourceCodeGenerator
import slick.{model => m}

slickCodegenSettings
slickCodegenDatabaseUrl := dbUrl
slickCodegenDatabaseUser := dbUser
slickCodegenDatabasePassword := dbPass
slickCodegenDriver := slick.driver.MySQLDriver
slickCodegenJdbcDriver := "com.mysql.cj.jdbc.Driver"
slickCodegenOutputPackage := "slick"
slickCodegenExcludedTables := Seq("schema_version")
slickCodegenOutputDir := file("./src/main/scala")
slickCodegenCodeGenerator := { (model: m.Model) =>
  new SourceCodeGenerator(model) {
    override def tableName =
      dbName => dbName.toCamelCase + "Table"
  }
}