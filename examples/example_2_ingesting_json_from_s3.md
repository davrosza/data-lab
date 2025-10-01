### Example 2: Ingesting JSON from S3 to Iceberg

**Upload test data to MinIO:**

```bash
# Create bucket via MinIO console or AWS CLI
aws --endpoint-url http://localhost:9000 s3 mb s3://iceberg

# Upload sample JSON
aws --endpoint-url http://localhost:9000 s3 cp sample.json s3://iceberg/data/
```

**Scala Pipeline** (`Pipeline2.scala`):

```scala
package pipelines

import scala.util.{Try, Success, Failure}
import org.apache.spark.sql.SparkSession
import common._

case class ExampleDTO(
  id: String,
  firstName: String,
  lastName: String,
  projects: Seq[ProjectDTO]
)

case class ProjectDTO(
  name: String,
  levelOfEffort: Double,
  department: Int
)

object Pipeline2 extends BasePipeline {
  val sparkSessionName = "Pipeline 2"
  val s3Path = "s3a://iceberg/data/*.json"

  def main(args: Array[String]): Unit = {
    implicit val spark = startSparkSession(sparkSessionName)
    import spark.implicits._

    val result = Try {
      spark.read
        .json(s3Path)
        .as[ExampleDTO]
        .writeTo(exampleTable)
        .append()
    }

    result match {
      case Success(_) =>
        println("Data appended successfully")
      case Failure(e) =>
        println(s"Failed to append data: ${e.getMessage}")
    }

    spark.stop()
  }
}
```

**Run locally:**

```bash
sbt pipeline2/assembly
java -jar src/main/scala/pipelines/pipeline2/target/scala-2.13/Pipeline2.jar
```
