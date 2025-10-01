package pipelines

object Pipeline1 extends BasePipeline {
    val sparkSessionName = "Pipeline 1"
    val exampleTable = "create_example_table.sql"
    
    def main(args: Array[String]): Unit = {
        implicit val spark = startSparkSession(sparkSessionName)

        createIcebergTable(exampleTable) match {
            case Success(_) => println("Example table created")
            case Failure(exception) => {
                println(
                    s"Unable to create Example table with error: $exception"
                )
                spark.stop()
                return
            }
        }
  }
}
