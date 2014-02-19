import play.Project._

name := "stormpath-play-sample"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	//Activator dependencies
	"org.webjars" %% "webjars-play" % "2.2.0", 
  	"org.webjars" % "bootstrap" % "2.3.1",
	///
	cache,
	"com.stormpath.sdk" % "stormpath-sdk-httpclient" % "0.9.2",
  	"com.stormpath.scala" % "stormpath-scala-core_2.10" % "0.1.0-SNAPSHOT"
	// Add your own project dependencies in the form:
  	// "group" % "artifact" % "version"
)

playScalaSettings
