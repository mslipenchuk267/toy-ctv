#!/usr/bin/env bash
set -euo pipefail

export PGPASSWORD="${POSTGRES_PASSWORD:-}"
SQL_DIR="${SQL_DIR:-/postgres/init}"

shopt -s nullglob
files=("$SQL_DIR"/*.sql)
if (( ${#files[@]} == 0 )); then
  echo "No .sql files in $SQL_DIR; nothing to apply."
  exit 0
fi

for f in "${files[@]}"; do
  echo "-> $f"
  psql \
    -h "${POSTGRES_HOST:-postgres}" \
    -p "${POSTGRES_PORT:-5432}" \
    -U "${POSTGRES_USER:-postgres}" \
    -d "${POSTGRES_DB:-postgres}" \
    -v ON_ERROR_STOP=1 \
    -f "$f"
done

echo "Postgres init complete."
