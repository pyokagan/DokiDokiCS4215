import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "cs4215"
ThisBuild / organizationName := "cs4215"

lazy val root = (project in file("."))
  .settings(
    name := "DokiDokiCS4215",
    libraryDependencies += scalaTest % Test,
  )
