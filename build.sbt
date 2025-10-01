name := "Subproject layout in SBT"
version := "0.1"
scalaVersion := "2.12.20"

val commonDependencies = Seq(
    "org.apache.spark" %% "spark-core" % "3.5.4" % "provided",
    "org.apache.spark" %% "spark-sql" % "3.5.4" % "provided",
    "org.apache.iceberg" % "iceberg-spark-runtime-3.5_2.13" % "1.7.1" % "provided",
    "org.apache.hadoop" % "hadoop-aws" % "3.3.1" % "provided",
    "com.amazonaws" % "aws-java-sdk-bundle" % "1.2.300" % "provided",
)

lazy val root = project
    .in(file("."))
    .settings(
        name := "root",
        Compile / scalaSource := baseDirectory.value / "src" / "main" / "scala",
        Compile / resourceDirectory := baseDirectory.value / "src" / "main" / "resources",
        Test / scalaSource := baseDirectory.value / "src" / "test" / "scala",
        libraryDependencies ++= commonDependencies
    )
    .aggregate(
        common,
        pipeline1,
        pipeline2
    )

lazy val common = 
    project
        .in(file("src/main/scala/common"))
        .settings(name := "Common", libraryDependencies ++= commonDependencies)

lazy val pipeline1 = 
    project
        .in(file("src/main/scala/pipelines/pipeline1"))
        .dependsOn(common)
        .settings(
            name := "Pipeline1",
            Compile / unmanagedResourceDirectories := Seq(
                (ThisBuild / baseDirectory).value / "src" / "main" / "resources",
            ),
            libraryDependencies ++= commonDependencies,
            assembly / assemblyJarName := "Pipeline1.jar"
        )
        .enablePlugins(AssemblyPlugin)

lazy val pipeline2 = 
    project
        .in(file("src/main/scala/pipelines/pipeline2"))
        .dependsOn(common)
        .settings(
            name := "Pipeline2",
            Compile / unmanagedResourceDirectories := Seq(
                (ThisBuild / baseDirectory).value / "src" / "main" / "resources",
            ),
            libraryDependencies ++= commonDependencies,
            assembly / assemblyJarName := "Pipeline2.jar"
        )
        .enablePlugins(AssemblyPlugin)

assembly / assemblyMergeStrategy := (
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
)