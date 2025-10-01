package pipelines

import scala.util.{Success, Failure}
import common._

object Pipeline1 extends BasePipeline {
    val sparkSessionName = "Pipeline 1"
    val exampleTableFile = "create_example_table.sql"
    
    def main(args: Array[String]): Unit = {
        implicit val spark = startSparkSession(sparkSessionName)

        createIcebergTable(exampleTableFile) match {
            case Success(_) => println("Example table created")
            case Failure(exception) => {
                println(
                    s"Unable to create Example table with error: $exception"
                )
            }
        }
        spark.stop()
  }
}
