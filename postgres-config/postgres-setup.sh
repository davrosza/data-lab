#!/bin/bash

set -e
set -u

psql -v ON_ERROR_STOP=0 --username "$POSTGRES_USER" <<-EOSQL
CREATE DATABASE airflow;
CREATE USER airflow WITH PASSWORD 'airflow';
CREATE DATABASE airflow OWNER TO airflow;
EOSQL