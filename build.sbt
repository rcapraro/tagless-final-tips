ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)

lazy val root = (project in file("."))
  .settings(
    name := "tagless-final-tips",
    libraryDependencies ++= Seq(
      "tf.tofu" %% "tofu-kernel" % "0.11.1",
      "tf.tofu" %% "tofu-core-ce3" % "0.11.1",
      "org.typelevel" %% "cats-effect" % "3.4.5",
      "org.typelevel" %% "cats-mtl" % "1.3.0",
    )
  )