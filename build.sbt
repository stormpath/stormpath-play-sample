import play.Project._

name := "stormpath-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	cache,
	"com.stormpath.sdk" % "stormpath-sdk-api" % "0.9.0",
	"com.stormpath.sdk" % "stormpath-sdk-httpclient" % "0.9.0",
  	"com.stormpath.scala" % "stormpath-scala-core_2.10" % "0.1.0-SNAPSHOT"
	// Add your own project dependencies in the form:
  	// "group" % "artifact" % "version"
)

playScalaSettings
