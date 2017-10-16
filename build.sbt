name := "HaveANiceDay"
version := Versions.project
scalaVersion := Versions.scala

mainClass in(Compile, run) := Some("finatra.HaveANiceDayServerMain")

enablePlugins(DockerPlugin)
enablePlugins(ScalafmtPlugin)
CommandAliases.addCommandAliases()

libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra
libraryDependencies += "ch.qos.logback" % "logback-classic" % Versions.logback
libraryDependencies += "com.h2database" % "h2" % Versions.flyway
libraryDependencies += "mysql" % "mysql-connector-java" % Versions.mysqlConnector
libraryDependencies += "com.typesafe" % "config" % Versions.config
libraryDependencies += "org.scalaj" %% "scalaj-http" % Versions.scalajHttp
libraryDependencies += "io.circe" %% "circe-core" % Versions.circe
libraryDependencies += "io.circe" %% "circe-generic" % Versions.circe
libraryDependencies += "io.circe" %% "circe-parser" % Versions.circe
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % Versions.slick,
  "com.typesafe.slick" %% "slick-hikaricp" % Versions.slick
)
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % Versions.slick
libraryDependencies += "com.danielasfregola" %% "twitter4s" % Versions.twitter4s
libraryDependencies += "org.quartz-scheduler" % "quartz" % Versions.quartz
libraryDependencies += "org.quartz-scheduler" % "quartz-jobs" % Versions.quartz
libraryDependencies += "com.h2database" % "h2" % Versions.h2database % Test
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

parallelExecution in Test := false
parallelExecution in IntegrationTest := false
testForkedParallel in Test := false
testForkedParallel in IntegrationTest := false
concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

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

test in assembly := {}

assemblyMergeStrategy in assembly := {
  case "BUILD" => MergeStrategy.discard
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case other => MergeStrategy.defaultMergeStrategy(other)
}

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}