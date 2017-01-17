//import io.michaelallen.mustache.sbt.Import.MustacheKeys

import sbt.Keys._
import sbt._



  val appName = "dsl-parser"
  val appVersion = "1.0"

  val scala = "2.10.4"
  val salat = "1.9.5"

  val json4s = "3.2.5"
  // other settings

  incOptions := incOptions.value.withNameHashing(true)

  val appDependencies = Seq(
    "org.scala-lang.modules" %% "scala-async" % "0.9.0-M4",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "org.json4s" %% "json4s-native" % json4s exclude("org.mockito", "mockito-all"),
    "org.json4s" %% "json4s-ext" % json4s exclude("org.mockito", "mockito-all"),
    "commons-codec" % "commons-codec" % "1.9",
    "com.github.nscala-time" %% "nscala-time" % "0.4.0",
    "net.logstash.logback" % "logstash-logback-encoder" % "3.0"
  )

  lazy val main = Project(appName, file("product"))
    .settings(
      version := appVersion,
      libraryDependencies ++= appDependencies,
      scalaVersion := "2.10.4",
      scalacOptions ++= Seq("-feature", "-language:_", "-Xfatal-warnings"),
      parallelExecution in Test := false
    )
    .settings(javaOptions in Test ++= Seq("-Xmx1024M", "-XX:+CMSClassUnloadingEnabled"))
    .settings(
      publishArtifact in(Compile, packageDoc) := false,
      publishArtifact in packageDoc := false,
      sources in(Compile, doc) := Seq.empty
    )
    .settings(javaOptions += "-Xmx333M")


  lazy val root = project.in(file("."))


   def rootProject = Some(main)



