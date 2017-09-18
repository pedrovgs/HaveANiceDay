name := "HaveANiceDay"
version := Versions.project
scalaVersion := Versions.scala

mainClass in (Compile,run) := Some("finatra.HaveANiceDayServerMain")

enablePlugins(ScalafmtPlugin)
CommandAliases.addCommandAliases()

libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra
libraryDependencies += "ch.qos.logback" % "logback-classic" % Versions.logback
libraryDependencies += "com.jakehschwartz" %% "finatra-swagger" % Versions.finatraSwagger
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

        