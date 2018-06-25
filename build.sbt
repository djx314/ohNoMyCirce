scalaVersion := "2.12.5"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

resolvers += Resolver.jcenterRepo

resolvers += Resolver.bintrayRepo("djx314", "maven")

bintrayPackageLabels := Seq("scala", "poi")

crossScalaVersions := Seq("2.12.6", "2.11.12")

scalacOptions ++= Seq("-feature", "-deprecation")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val ohNoMyCirce = (project in file("."))


organization := "net.scalax"

name := "ohNoMyCirce"