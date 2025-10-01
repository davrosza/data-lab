### Example 1: Creating an Iceberg Table

**SQL Definition** (`src/main/resources/SQL/create_example_table.sql`):

```sql
CREATE TABLE IF NOT EXISTS spark_catalog.default.example_table (
  id STRING,
  firstName STRING,
  lastName STRING,
  projects ARRAY<STRUCT
    name: STRING,
    levelOfEffort: DOUBLE,
    department: INT
  >>
)
USING iceberg
PARTITIONED BY (days(id))
```

**Scala Pipeline** (`Pipeline1.scala`):

```scala
package pipelines

import scala.util.{Success, Failure}
import common._

object Pipeline1 extends BasePipeline {
  val sparkSessionName = "Pipeline 1"

  def main(args: Array[String]): Unit = {
    implicit val spark = startSparkSession(sparkSessionName)

    createIcebergTable("create_example_table.sql") match {
      case Success(_) =>
        println("Example table created successfully")
      case Failure(exception) =>
        println(s"Failed to create table: ${exception.getMessage}")
        spark.stop()
        sys.exit(1)
    }

    spark.stop()
  }
}
```

**Run locally:**

```bash
sbt pipeline1/assembly
java -jar src/main/scala/pipelines/pipeline1/target/scala-2.13/Pipeline1.jar
```
