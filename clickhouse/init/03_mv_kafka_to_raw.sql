CREATE MATERIALIZED VIEW IF NOT EXISTS ctv.mv_kafka_to_raw
TO ctv.ctv_raw_events
AS
SELECT * FROM ctv.kafka_ctv_events;
