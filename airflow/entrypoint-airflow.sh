#!/usr/bin/env bash

airflow db init
airflow users create \
    --username $AIRFLOW_USER \
    --firstname FIRST_NAME \
    --lastname LASTNAME \
    --role Admin \ 
    --email 'admin@example.com' \
    --password $AIRFLOW_PASSWORD

airflow connections add 'spark-local' \
    --conn-type 'spark' \
    --conn-host 'spark://spark-master' \ 
    --conn-port '7077'

echo "Starting $1"
exec airflow "$1"