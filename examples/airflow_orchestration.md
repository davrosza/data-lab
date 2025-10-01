### Example 3: Orchestrating with Airflow

**Airflow DAG** (`dags/example_dag.py`):

```python
from airflow import DAG
from airflow.providers.apache.spark.operators.spark_submit import SparkSubmitOperator
from airflow.models import Variable
from datetime import datetime, timedelta

def create_spark_job(dag, spark_job_name, parameters=None):
    """Factory function for environment-aware Spark jobs"""

    default_params = {
        "driver_memory": "1g",
        "executor_memory": "2g",
        "num_executors": "2"
    }

    job_params = {**default_params, **(parameters or {})}

    # Environment-specific configuration
    env = Variable.get("environment", default_var="local")
    if env == "local":
        conn_id = "spark_local"
        conf = {
            "spark.hadoop.fs.s3a.endpoint": "http://minio:9000",
            "spark.hadoop.fs.s3a.access.key": "admin",
            "spark.hadoop.fs.s3a.secret.key": "admin",
            "spark.hadoop.fs.s3a.path.style.access": "true",
            "spark.hadoop.fs.s3a.impl": "org.apache.hadoop.fs.s3a.S3AFileSystem"
        }
    else:
        conn_id = "spark_prod"
        conf = {}

    return SparkSubmitOperator(
        task_id=f"spark_job_{spark_job_name}",
        application=f"/opt/airflow/dags/jars/{spark_job_name}.jar",
        conn_id=conn_id,
        conf=conf,
        java_class=f"pipelines.{spark_job_name}",
        dag=dag,
        retries=3,
        retry_delay=timedelta(minutes=5),
        verbose=True,
        **job_params
    )

default_args = {
    'owner': 'data-team',
    'depends_on_past': False,
    'start_date': datetime(2025, 1, 1),
    'email_on_failure': False,
    'email_on_retry': False,
}

with DAG(
    'example_pipeline',
    default_args=default_args,
    description='Create tables and ingest data',
    schedule_interval='@daily',
    catchup=False
) as dag:

    create_table = create_spark_job(
        dag=dag,
        spark_job_name="Pipeline1"
    )

    ingest_data = create_spark_job(
        dag=dag,
        spark_job_name="Pipeline2",
        parameters={
            "driver_memory": "2g",
            "executor_memory": "4g"
        }
    )

    create_table >> ingest_data
```

**Trigger the DAG:**

```bash
# Via Airflow UI at http://localhost:8081
# Or via CLI:
docker-compose exec airflow airflow dags trigger example_pipeline
```
