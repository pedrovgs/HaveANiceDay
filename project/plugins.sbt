addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "0.3")
addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.2.0")
addSbtPlugin("com.github.tototoshi" % "sbt-slick-codegen" % "1.2.1")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.5.0")

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.7-dmr"
resolvers += "Flyway" at "https://flywaydb.org/repo"
