# toy-ctv

End-to-end toy pipeline for **CTV ad beacons → Kafka → ClickHouse → Grafana**.  
Kafka & ClickHouse run via Docker Compose; Kafka topic is created with a 1-hour retention using a tiny Admin API script run with **uv** (kept under `test/`).  

---

## Prereqs

- **Docker** & **Docker Compose v2**
- **uv** (https://github.com/astral-sh/uv)
- macOS/Linux shell

---

## Quick start (TL;DR)

```bash
# 1) Bring up the stack
docker compose up -d

# 2) Initialize Kafka topic (1h retention) and ClickHouse schema
./scripts/init.sh

# 3) Open Kafbat Kafka UI
open http://localhost:8080

# 3) Produce some toy CTV events to Kafka using uv (from test/)
cd test
uv run python /test_runner.py
cd ..

# 4) Open Grafana and explore
open http://localhost:3000
# login: admin / admin  (datasource "ClickHouse" is pre-provisioned)