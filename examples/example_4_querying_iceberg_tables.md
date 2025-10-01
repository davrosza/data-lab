### Example 4: Querying Iceberg Tables

**Using Spark SQL:**

```scala
spark.sql("""
  SELECT
    firstName,
    lastName,
    size(projects) as num_projects,
    aggregate(projects, 0.0, (acc, p) -> acc + p.levelOfEffort) as total_effort
  FROM spark_catalog.default.example_table
  WHERE id IS NOT NULL
""").show()
```

**Time Travel:**

```scala
// Query table as of yesterday
spark.sql("""
  SELECT *
  FROM spark_catalog.default.example_table
  TIMESTAMP AS OF '2025-10-01 00:00:00'
""").show()

// Query specific snapshot
spark.sql("""
  SELECT *
  FROM spark_catalog.default.example_table
  VERSION AS OF 12345678901234
""").show()
```

**Schema Evolution:**

```sql
-- Add new column without rewriting data
ALTER TABLE spark_catalog.default.example_table
ADD COLUMN email STRING;

-- Existing data will have NULL for new column
-- New writes can populate it
```
