# toy-ctv

## How it flows

End-to-end toy **CTV ad-tech** pipeline following STAR schema: **PostgreSQL** hosts  **dimension tables** (advertisers, campaigns, creatives) which are **replicated into ClickHouse** via the `MaterializedPostgreSQL` engine at SCD-1 CDC. **Kafka CTV events** are ingested with the **ClickHouse** `Kafka` engine and appended into a fact table (`MergeTree` engine) via a materialized view. Grafana lets you query Clickhouse & Postgres from one place.

- **OLTP (Dim source):** PostgreSQL (`public.advertisers`, `public.campaigns`, `public.creatives`)
- **Streaming (Fact ingress):** Kafka (`dev.ctv.ad-beacons.raw.v1`)
- **OLAP (Query):** ClickHouse (`ctv.ctv_raw_events` + local dims)
- **UI:** Grafana (pre-provisioned ClickHouse datasource)

> Up: `docker compose up -d`  
> Down (fresh reset): `docker compose down -v`  
> Generate events: `cd test && uv run python func_test.py`

---

## How it flows

```text
   (CTV beacons)
     Devices
       │
       ▼
┌───────────────────┐            ┌────────────────────────────────────────────────────┐
│      Kafka        │            │                     PostgreSQL                     │
│     <topic>       │            │                 <dimension tables>                 │
└─────────┬─────────┘            └───────────────┬────────────────────────────────────┘
          │                                      │
          │ (ingest events)                      │ (logical replication / SCD-1 upserts)
          ▼                                      ▼
┌───────────────────┐            ┌─────────────────────────────────────────────────────┐
│    ClickHouse     │◀──────────▶│        (MaterializedPostgreSQL) <dim. tables>       │
│   <fact tables>   │            └─────────────────────────────────────────────────────┘
└───────────────────┘                           
                           ┌──────────────────────┐
                           │       Grafana        │
                           │                      │
                           └─────────▲────────────┘
                                     │
                               Explore & Dashboards

```
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

# 2) Observability, SQL queries against OLTP/OLAP DBs
# Kafbat - Kafka Observability
open http://localhost:8080
# Grafana - Clickhouse & Postgres Datasources setup for SQL queries 
# login: admin , pass: admin
open http://localhost:3000 

# 3) Produce some test CTV events to Kafka
cd test
uv run python /test_runner.py
cd ..

```

---

## Cleanup

```bash
docker compose down -v
```