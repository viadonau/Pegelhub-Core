#!/bin/bash

set -e
influx user create --name "$STORE_USER" --password "$STORE_PASSWORD"

influx bucket create --name "$BUCKET_TELEMETRY"
influx bucket create --name "$BUCKET_DATA"

TOKEN=$(influx auth create --user "$STORE_USER" --read-buckets --write-buckets | tail -n -1 | cut -f 4)
OUT_FILE="/etc/influxdb2/store/storeapp.yaml"
touch $OUT_FILE
echo "data:" > $OUT_FILE
echo "  org: $DOCKER_INFLUXDB_INIT_ORG" >> $OUT_FILE
echo "  token: $TOKEN" >> $OUT_FILE
echo "  bucket: $BUCKET_DATA" >> $OUT_FILE
echo "  url: none" >> $OUT_FILE
echo "telemetry:" >> $OUT_FILE
echo "  org: $DOCKER_INFLUXDB_INIT_ORG" >> $OUT_FILE
echo "  token: $TOKEN" >> $OUT_FILE
echo "  bucket: $BUCKET_TELEMETRY" >> $OUT_FILE
echo "  url: none" >> $OUT_FILE
