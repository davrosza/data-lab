package pipelines

import com.amazonaws.services.s3.{AmazonS3ClientBuilder, AmazonS3}
import com.amazonaws.auth.DefaultAWSCrednetialsProviderChain
import com.amazonaws.ClientConfiguration
import com.amazonaws.client.builder.AWSClientBuilder

object Pipeline2 extends BasePipeline {
  val sparkSessionName = "Pipeline 2"

  implicit val s3Client: AmazonS3 = AmazonS3ClientBuilder
    .standard()
    .withCredentials(new DefaultAWSCrednetialsProviderChain())
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
    implicit val spark = startSparkSession()
  }
}
