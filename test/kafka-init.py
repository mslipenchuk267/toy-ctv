#!/usr/bin/env python3
import sys
from confluent_kafka.admin import (
    AdminClient, NewTopic, ConfigResource, ConfigEntry, AlterConfigOpType
)

def ensure_topic(bootstrap: str, topic: str, hours: int = 1) -> None:
    retention_ms = str(hours * 3600 * 1000)
    admin = AdminClient({"bootstrap.servers": bootstrap})

    # 1) Create if missing (1 partition, rf=1) with desired configs
    nt = NewTopic(
        topic,
        num_partitions=1,
        replication_factor=1,
        config={"cleanup.policy": "delete", "retention.ms": retention_ms},
    )
    fut = admin.create_topics([nt])[topic]
    try:
        fut.result()
        print(f"created '{topic}' (1p, rf=1, retention.ms={retention_ms})")
    except Exception as e:
        # fine if it already exists; we’ll enforce configs next
        print(f"create_topics: {e}; ensuring configs…")

    # 2) Incremental alter configs (no deprecation)
    cr = ConfigResource(
        ConfigResource.Type.TOPIC,
        topic,
        incremental_configs=[
            ConfigEntry("cleanup.policy", "delete",
                        incremental_operation=AlterConfigOpType.SET),
            ConfigEntry("retention.ms", retention_ms,
                        incremental_operation=AlterConfigOpType.SET),
        ],
    )
    admin.incremental_alter_configs([cr])[cr].result()
    print(f"set cleanup.policy=delete, retention.ms={retention_ms} on '{topic}'")
    print("Kafka done.")

if __name__ == "__main__":
    # Usage:
    #   uv run --with confluent-kafka python Kafka-init.py [bootstrap] [topic] [hours]
    # Defaults: localhost:9092, dev.ctv.ad-beacons.raw.v1, 1
    bootstrap = sys.argv[1] if len(sys.argv) > 1 else "localhost:9092"
    topic     = sys.argv[2] if len(sys.argv) > 2 else "dev.ctv.ad-beacons.raw.v1"
    hours     = int(sys.argv[3]) if len(sys.argv) > 3 else 1
    ensure_topic(bootstrap, topic, hours)
