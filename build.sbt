ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.4.0"
libraryDependencies += "org.playframework" %% "play-json" % "3.0.6"

lazy val root = (project in file("."))
  .settings(
    name := "Thirty-One"
  )