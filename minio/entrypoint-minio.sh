#!/bin/bash

/usr/bin/mc alias set local http://minio:9000 $AWS_ACCESS_KEY_ID $AWS_SECRET_ACCESS_KEY

# Creates an iceberg bucket
/usr/bin/mc mb local/iceberg --ignore-existing