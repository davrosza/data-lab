## Development Workflow

### Prerequisites

- **Docker** (20.10+) and **Docker Compose** (2.0+)
- **SBT** (1.9+)
- **Java** (11 or 17)
- **Scala** (2.13.15)

### 1. Create a New Pipeline

```bash
# Create directory structure
mkdir -p src/main/scala/pipelines/pipeline3

# Create Scala file
cat > src/main/scala/pipelines/pipeline3/Pipeline3.scala << 'EOF'
package pipelines

import common._

object Pipeline3 extends BasePipeline {
  val sparkSessionName = "Pipeline 3"

  def main(args: Array[String]): Unit = {
    implicit val spark = startSparkSession(sparkSessionName)

    // Your pipeline logic here

    spark.stop()
  }
}
EOF
```

### 2. Add Subproject to build.sbt

```scala
lazy val pipeline3 = project
  .in(file("src/main/scala/pipelines/pipeline3"))
  .dependsOn(common)
  .settings(
    name := "Pipeline3",
    Compile / unmanagedResourceDirectories := Seq(
      (ThisBuild / baseDirectory).value / "src" / "main" / "resources"
    ),
    libraryDependencies ++= commonDependencies,
    assembly / assemblyJarName := "Pipeline3.jar"
  )
  .enablePlugins(AssemblyPlugin)

// Update root project aggregate
lazy val root = project
  .in(file("."))
  .aggregate(common, pipeline1, pipeline2, pipeline3)
```

### 3. Build and Test

```bash
# Compile to check for errors
sbt pipeline3/compile

# Run tests if you have them
sbt pipeline3/test

# Build fat JAR
sbt pipeline3/assembly

# Test locally
java -jar src/main/scala/pipelines/pipeline3/target/scala-2.13/Pipeline3.jar
```

### 4. Add to Airflow

```bash
# Copy JAR
cp src/main/scala/pipelines/pipeline3/target/scala-2.13/Pipeline3.jar dags/jars/

# Update DAG or create new one
# See Example 3 above for DAG patterns
```

You can also mount a directory to airflow where the JAR and Python (PySpark) files will live.
