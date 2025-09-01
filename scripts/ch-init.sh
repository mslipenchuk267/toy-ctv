#!/usr/bin/env bash
set -euo pipefail

SQL_DIR="${SQL_DIR:-/clickhouse/init}"
CH_HOST="${CLICKHOUSE_HOST:-clickhouse}"
CH_PORT="${CLICKHOUSE_PORT:-9000}"
CH_USER="${CLICKHOUSE_USER:-default}"
CH_PASS="${CLICKHOUSE_PASSWORD:-mysecret}"
CH_DB="${CLICKHOUSE_DB:-default}"

echo "Applying ClickHouse schema to ${CH_HOST}:${CH_PORT}/${CH_DB} ..."
shopt -s nullglob
files=("$SQL_DIR"/*.sql)
if (( ${#files[@]} == 0 )); then
  echo "No .sql files found in $SQL_DIR; nothing to apply."
  exit 0
fi

for f in "${files[@]}"; do
  echo "-> $f"
  if [[ -n "$CH_PASS" ]]; then
    clickhouse-client --host "$CH_HOST" --port "$CH_PORT" \
      -u "$CH_USER" --password "$CH_PASS" --database "$CH_DB" \
      --multiquery < "$f"
  else
    clickhouse-client --host "$CH_HOST" --port "$CH_PORT" \
      -u "$CH_USER" --database "$CH_DB" \
      --multiquery < "$f"
  fi
done

echo "ClickHouse init complete."
