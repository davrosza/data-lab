package pipelines

import scala.util.{Try, Success, Failure}
import com.amazonaws.services.s3.{AmazonS3ClientBuilder, AmazonS3}
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.ClientConfiguration
import com.amazonaws.client.builder.AWSClientBuilder
import common._

object Pipeline2 extends BasePipeline {
  val sparkSessionName = "Pipeline 2"
  val s3Path = "s3a://iceberg/path/*.json"

  implicit val s3Client: AmazonS3 = AmazonS3ClientBuilder
    .standard()
    .withCredentials(new DefaultAWSCredentialsProviderChain())
    .withClientConfiguration(
        new ClientConfiguration().withSignerOverride("AWSS3V4SignerType")
    )
    .withEndpointConfiguration(
        new AWSClientBuilder.EndpointConfiguration(
            "http://minio:9000",
            "us-east-1"
        )
    )
    .enablePathStyleAccess()
    .build()


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
      case Success(_) => println("Data appended successfully")
      case Failure(e) => println(s"Failed to append data: ${e.getMessage}")
    }
    
    spark.stop()
  }
}
