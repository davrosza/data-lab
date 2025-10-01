# Data Lab: A local data environment

A containerized data platform for local development to test data pipelines before deployment. The core components are:

- **Apache Spark (3.5.4)** - Distributed data processing with Scala-based jobs
- **MinIO** - S3-compatible object storage serving as the data lake
- **Apache Iceberg (1.7.1)** - Lakehouse table format providing ACID transactions, schema evolution, and time travel
- **Apache Airflow** - Workflow orchestration with environment-aware DAG generation
- **PostgreSQL** - Dual purpose as data warehouse and Airflow's metadata store

## Design Decisions

### Why Subprojects Over Monolithic JARs?

When managing multiple Spark jobs in a single repository, teams often face a common architectural challenge: how to structure the build system. The default approach typically results in a single massive JAR containing all code, which leads to slow deployment times, difficult dependency management, and an increased risk of classpath conflicts. As the codebase grows, these issues compound, making it harder to iterate quickly and deploy changes safely.\

The solution implemented here uses SBT subprojects, where each pipeline compiles independently while sharing common code through a dedicated common module. This approach produces smaller artifacts containing only what each job needs, resulting in faster deployments and easier debugging. The clear separation of concerns means each pipeline can be independently tested and deployed without affecting others.

However, this architectural choice comes with tradeoffs. The build configuration becomes more complex, requiring explicit dependency management and careful coordination between subprojects. The initial setup time is also slightly longer compared to a simple single-project build. Despite these costs, the benefits in maintainability and operational efficiency make subprojects the superior choice for any multi-job Spark repository.

### Why Iceberg Over Raw Parquet?

All tables in this project use Apache Iceberg format rather than raw Parquet files, a decision that fundamentally changes how data is managed. Iceberg provides ACID transactions that prevent partial writes, ensuring data consistency even when jobs fail mid-execution. Schema evolution becomes trivial—new columns can be added without rewriting existing data, and the format tracks schema changes over time. Time travel capabilities enable auditing and debugging by allowing queries against historical table states, while hidden partitioning eliminates the user errors that commonly arise from manual partition management.

These production-grade guarantees come at the cost of slightly more complex initial setup compared to simply writing Parquet files. There's also a small metadata overhead for each transaction as Iceberg maintains its catalog of snapshots and manifests. However, the dramatically simpler partition management and the confidence that comes from ACID guarantees make Iceberg the clear choice for any serious data platform, even in local development environments.

## Getting Started

### Installation

**1. Clone the repository:**

```bash
git clone https://github.com/yourusername/data-lab.git
cd data-lab
```

**2. Start the infrastructure:**

```bash
docker-compose up -d
```

**3. Verify services are running:**

```bash
docker-compose ps
```

You should see all services in "Up" state.

**4. Build Spark jobs:**

```bash
# Build all projects
sbt assembly

# Or build specific pipeline
sbt pipeline1/assembly
sbt pipeline2/assembly
```

JARs will be created at:

- `src/main/scala/pipelines/pipeline1/target/scala-2.13/Pipeline1.jar`
- `src/main/scala/pipelines/pipeline2/target/scala-2.13/Pipeline2.jar`

Note that the pipelines can be written in PySpark and Scala Spark.

**5. Copy JARs to Airflow:**

```bash
mkdir -p dags/jars
cp src/main/scala/pipelines/*/target/scala-2.13/*.jar dags/jars/
```

### Access Points

Defaults can be configure in docker-compose.yml

- **MinIO Console:** http://localhost:9001
  - Username: `admin`
  - Password: `admin`
- **Spark Master UI:** http://localhost:8080
  - View cluster status and running jobs
- **Airflow UI:** http://localhost:8081

  - Username: `admin`
  - Password: `admin`

- **PostgreSQL:**
  - Host: `localhost:5432`
  - Database: `postgres` and `airflow`
  - Username: `postgres` and `airflow`
  - Password: `postgres` and `airflow`
