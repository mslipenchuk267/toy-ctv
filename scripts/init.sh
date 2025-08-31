#!/usr/bin/env bash
set -e

# Run Kafka topic init via uv in test/
cd test/
uv run python Kafka-init.py \
  localhost:9092 \
  dev.ctv.ad-beacons.raw.v1 \
  1
cd ..
# Apply ClickHouse schema
./scripts/ch-init.sh

echo "init done."
