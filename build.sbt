name := "Subproject layout in SBT"
version := "0.1"
scalaVersion := "2.12.20"

val commonDependencies = Seq()

lazy val root = project
    .in(file("."))
    .settings(
        name := "root",
        Compile / scalaSource := baseDirectory.value / "src" / "main" / "scala"
    )