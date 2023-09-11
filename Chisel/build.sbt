ThisBuild / scalaVersion     := "2.13.8"

val chiselVersion = "3.5.6"

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "org.scalatest" %% "scalatest" % "3.1.4" % "scala_test",
      "edu.berkeley.cs" %% "chiseltest" % "0.5.6" % "test"
    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      "-P:chiselplugin:genBundleElements",
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full),
  )