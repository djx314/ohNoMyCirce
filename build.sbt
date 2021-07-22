licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

val scalaVersionStr = "2.13.6"
scalaVersion := scalaVersionStr

crossScalaVersions := Seq("2.12.14", scalaVersionStr)

scalacOptions ++= Seq("-feature", "-deprecation")

scalafmtOnCompile := true

val circeVersion = "0.14.1"
val slickVersion = "3.3.3"
val scalatestVersion = "3.2.9"
val playJsonVersion = "2.9.2"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"          % slickVersion,
  "org.slf4j"           % "slf4j-nop"      % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect"  % scalaVersion.value,
  "org.scala-lang" % "scala-compiler" % scalaVersion.value
)

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test"
)

libraryDependencies += "com.typesafe.play" %% "play-json" % playJsonVersion

val ohNoMyCirce = project in file(".")

organization := "net.scalax"

name := "ohNoMyCirce"

Test / fork := true
