CREATE TABLE IF NOT EXISTS ctv.kafka_ctv_events
(
    schema_version String,
    event_type     String,
    event_id       String,
    ts_ms          UInt64,
    ingested_at    String,

    session_id       String,
    ad_pod_id        String,
    ad_pod_position  UInt8,
    ad_pod_len       UInt8,
    sequence_index   UInt32,

    device_make        String,
    device_model       String,
    device_os          String,
    device_os_version  String,
    ifa_type           String,
    ifa                String,
    limit_ad_tracking  UInt8,
    user_agent         String,

    app_bundle     String,
    app_name       String,
    app_version    String,
    player_name    String,
    player_version String,

    channel_name   String,
    content_id     String,
    content_title  String,
    is_live        UInt8,

    ad_id          String,
    creative_id    String,
    ad_duration_ms UInt32,
    ad_skippable   UInt8,
    ad_skip_offset_ms Nullable(UInt32),

    autoplay    UInt8,
    fullscreen  UInt8,
    muted       UInt8,
    playhead_ms UInt32
)
ENGINE = Kafka
SETTINGS
    kafka_broker_list   = 'kafka:9093',
    kafka_topic_list    = 'dev.ctv.ad-beacons.raw.v1',
    kafka_group_name    = 'ch-ctv-consumer',
    kafka_format        = 'JSONEachRow',
    kafka_num_consumers = 1;
