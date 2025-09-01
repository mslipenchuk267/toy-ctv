#!/usr/bin/env bash
set -euo pipefail

# ---- config / defaults ----
KAFKA_BIN="${KAFKA_BIN:-/opt/kafka/bin}"
PATH="$KAFKA_BIN:$PATH"

BROKERS="${KAFKA_BROKERS:?KAFKA_BROKERS required (e.g., broker:29092)}"
TOPIC="${TOPIC_NAME:?TOPIC_NAME required}"
PARTS="${TOPIC_PARTITIONS:-1}"
REPL="${TOPIC_REPLICATION:-1}"
HOURS="${TOPIC_RETENTION_HOURS:-1}"
POLICY="${CLEANUP_POLICY:-delete}"
EXTRA="${EXTRA_CONFIGS:-}"

retention_ms=$(( HOURS * 3600 * 1000 ))

# Resolve CLI names (some distros ship without *.sh suffix)
TOPICS_BIN="${KAFKA_BIN}/kafka-topics.sh";  [[ -x "${KAFKA_BIN}/kafka-topics"  ]] && TOPICS_BIN="${KAFKA_BIN}/kafka-topics"
CONFIGS_BIN="${KAFKA_BIN}/kafka-configs.sh"; [[ -x "${KAFKA_BIN}/kafka-configs" ]] && CONFIGS_BIN="${KAFKA_BIN}/kafka-configs"

command -v "${TOPICS_BIN}" >/dev/null || { echo "Cannot find kafka-topics* in ${KAFKA_BIN}"; exit 127; }
command -v "${CONFIGS_BIN}" >/dev/null || { echo "Cannot find kafka-configs* in ${KAFKA_BIN}"; exit 127; }

# ---- fast readiness: TCP + short-metadata probe ----
BOOT_HOST="${BROKERS%:*}"
BOOT_PORT="${BROKERS##*:}"

echo "Waiting for Kafka TCP ${BOOT_HOST}:${BOOT_PORT} ..."
for i in {1..60}; do
  (exec 3<>"/dev/tcp/${BOOT_HOST}/${BOOT_PORT}") >/dev/null 2>&1 && break
  sleep 2
  [[ $i -eq 60 ]] && { echo "Timeout waiting for ${BROKERS}" >&2; exit 1; }
done
echo "TCP ready."

CLI_PROPS="$(mktemp)"
cat >"$CLI_PROPS" <<EOF
request.timeout.ms=5000
metadata.max.age.ms=3000
client.id=kafka-init
EOF

echo "Checking broker metadata at ${BROKERS} ..."
until "${TOPICS_BIN}" --bootstrap-server "${BROKERS}" --command-config "${CLI_PROPS}" --list >/dev/null 2>&1; do
  echo "Broker not ready for metadata; retrying..."
  sleep 2
done
echo "Broker metadata OK."

# ---- create topic if missing (idempotent) ----
echo "Ensuring topic '${TOPIC}' exists..."
"${TOPICS_BIN}" \
  --bootstrap-server "${BROKERS}" \
  --command-config "${CLI_PROPS}" \
  --create --if-not-exists \
  --topic "${TOPIC}" \
  --partitions "${PARTS}" \
  --replication-factor "${REPL}" \
  --config "cleanup.policy=${POLICY}" \
  --config "retention.ms=${retention_ms}" \
  $( [[ -n "${EXTRA}" ]] && printf -- '--config %q ' ${EXTRA//,/ } )

# ---- upsert configs each run (safe re-run) ----
echo "Applying configs to '${TOPIC}'..."
"${CONFIGS_BIN}" \
  --bootstrap-server "${BROKERS}" \
  --command-config "${CLI_PROPS}" \
  --alter \
  --topic "${TOPIC}" \
  --add-config "cleanup.policy=${POLICY},retention.ms=${retention_ms}${EXTRA:+,${EXTRA}}"

echo "Kafka init complete for topic '${TOPIC}'."
