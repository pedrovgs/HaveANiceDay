name := "HaveANiceDay"
version := "0.1"
scalaVersion := "2.12.3"
mainClass in (Compile,run) := Some("finatra.ServerMain")

enablePlugins(ScalafmtPlugin)
CommandAliases.addCommandAliases()

libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra
libraryDependencies += "com.twitter" %% "finatra-http" % Versions.finatra % Test

        