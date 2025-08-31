# ctv_producer.py
import json
from typing import Optional, Callable, Any, Dict

try:
    from confluent_kafka import Producer
except ImportError as e:
    raise SystemExit(
        "Missing dependency. With uv you can run:\n"
        "  uv run --with confluent-kafka python test_runner.py\n"
        "Or add it to pyproject:\n"
        "  uv add confluent-kafka\n"
    ) from e


class KafkaJSONProducer:
    def __init__(self, broker: str, topic: str,
                 on_delivery: Optional[Callable[[Optional[Exception], Any], None]] = None):
        self._topic = topic
        self._producer = Producer({"bootstrap.servers": broker})
        self._on_delivery = on_delivery or self._default_delivery

    @staticmethod
    def _default_delivery(err, msg):
        if err is not None:
            print(f"[delivery] ERROR: {err}")

    def produce(self, record: Dict[str, Any], key: Optional[str] = None):
        payload = json.dumps(record, ensure_ascii=False).encode("utf-8")
        while True:
            try:
                self._producer.produce(
                    self._topic,
                    value=payload,
                    key=(key.encode("utf-8") if isinstance(key, str) else key),
                    on_delivery=self._on_delivery,
                )
                break
            except BufferError:
                self._producer.poll(0.1)
        self._producer.poll(0)

    def flush(self, timeout: float = 10.0):
        self._producer.flush(timeout)
