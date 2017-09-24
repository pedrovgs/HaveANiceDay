name := "HaveANiceDay"
version := Versions.project
scalaVersion := Versions.scala

mainClass in (Compile,run) := Some("finatra.HaveANiceDayServerMain")

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

flywayUrl := "jdbc:mysql://localhost/haveaniceday"
flywayUser := "haveaniceday"
flywayPassword := "haveaniceday"