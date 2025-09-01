#!/usr/bin/env bash

# Exit immediately if a command exits with a non-zero status, treat unset variables as errors, and fail if any command in a pipeline fails.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CH_URL="${CLICKHOUSE_URL:-http://localhost:8123}"
CH_USER="${CLICKHOUSE_USER:-default}"
CH_PASS="${CLICKHOUSE_PASSWORD:-mysecret}"

echo "Applying ClickHouse schema to $CH_URL ..."
shopt -s nullglob
for f in "$ROOT/clickhouse/init/"*.sql; do
  echo "-> $f"
  if [ -n "$CH_PASS" ]; then
    curl -fsS "$CH_URL" -u "$CH_USER:$CH_PASS" --data-binary @"$f" >/dev/null
  else
    curl -fsS "$CH_URL" -u "$CH_USER" --data-binary @"$f" >/dev/null
  fi
done
echo "Clickhouse Done."
