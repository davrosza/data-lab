package common

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame

trait BasePipeline {
  val exampleTable = "spark_catalog.default.example_table"

  def startSparkSession(name: String): SparkSession = {
    val sparkSession = SparkSession.builder
        .appName(name)
        .config(
            "spark.sql.exensions",
            "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions"
        )
        .config(
            "spark.sql.catalog.spark_catalog",
            "org.apache.iceberg.spark.SparkSessionCatalog"
        )
        .config("spark.sql.catalog.spark_catalog.type", "hadoop")
        .config(
            "spark.sql.catalog.spark_catalog.warehouse",
            "s3a://iceberg/warehouse"
        )
        .config("spark.hadoop.fs.s3a.endpoint", "http://minio:9000")
        .getOrCreate()
    return sparkSession
  }

  def runQuery(query: String)(implict spark: SparkSession): Try[DataFrame] = {
    Try(spark.sql(query))
  }

  def loadQuery(path: String): Try[String] = {
    Using(Source.fromResource(s"SQL/$path")) { source => 
        source.mkString
    }
  }

  def createIcebergTable(tableName: String)(implict spark: SparkSession): Try[Unit] = {
    loadQuery(tableName).flatMap { query => 
        runQuery(query).map(_ => ())
    }
  }
}
