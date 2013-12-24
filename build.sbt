import play.Project._

name := "stormpath-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // Select Play modules
  //jdbc,      // The JDBC connection pool and the play.api.db API
  //anorm,     // Scala RDBMS Library
  //javaJdbc,  // Java database API
  //javaEbean, // Java Ebean plugin
  //javaJpa,   // Java JPA plugin
  //filters,   // A set of built-in filters
  javaCore,  // The core Java API
  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  "com.stormpath.sdk" % "stormpath-sdk-api" % "0.9.0",
  "com.stormpath.sdk" % "stormpath-sdk-httpclient" % "0.9.0",
  "com.stormpath.scala" % "stormpath-scala-core_2.10" % "1.0-SNAPSHOT"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

play.Project.playScalaSettings
//playJavaSettings
