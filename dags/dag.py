from airflow import DAG
from airflow.providers.apache.spark.operators.spark_submit import SparkSubmitOperator

default_dag_args = {"owner": "airflow"}

dag = DAG("example-dag", default_args=default_dag_args)

spark = SparkSubmitOperator(
    task_id="example",
    dag=dag,
    conn_id="spark-local",
    application="/opt/spark/jars/Example.jar",
    jars="/opt/bitnami/spark/jars/iceberg-spark-runtime-3.5_2.12-1.7.1.jar,/opt/bitnami/spark/jars/iceberg-spark-extensions-3.5_2.12-1.7.1.jar,/opt/bitnami/spark/jars/aws-java-sdk-bundle-1.12.300.jar",
)
