CREATE TABLE IF NOT
EXISTS spark_catalog.default.example_table (
    id STRING,
    first_name STRING,
    last_name STRING,
    projects ARRAY<STRUCT<
        name STRING,
        level_of_effort DOUBLE,
        department INT
    >>,
    PRIMARY KEY(id)
) USING iceberg