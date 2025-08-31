CREATE TABLE IF NOT EXISTS ctv.ctv_raw_events
(
    schema_version LowCardinality(String),
    event_type     LowCardinality(String),
    event_id       String,
    ts_ms          UInt64,
    ts             DateTime MATERIALIZED toDateTime(ts_ms/1000),
    ingested_at    String,

    session_id       String,
    ad_pod_id        String,
    ad_pod_position  UInt8,
    ad_pod_len       UInt8,
    sequence_index   UInt32,

    device_make        LowCardinality(String),
    device_model       LowCardinality(String),
    device_os          LowCardinality(String),
    device_os_version  LowCardinality(String),
    ifa_type           LowCardinality(String),
    ifa                String,
    limit_ad_tracking  UInt8,
    user_agent         String,

    app_bundle     LowCardinality(String),
    app_name       LowCardinality(String),
    app_version    LowCardinality(String),
    player_name    LowCardinality(String),
    player_version LowCardinality(String),

    channel_name   LowCardinality(String),
    content_id     String,
    content_title  String,
    is_live        UInt8,

    ad_id          String,
    creative_id    LowCardinality(String),
    ad_duration_ms UInt32,
    ad_skippable   UInt8,
    ad_skip_offset_ms Nullable(UInt32),

    autoplay    UInt8,
    fullscreen  UInt8,
    muted       UInt8,
    playhead_ms UInt32
)
ENGINE = MergeTree
PARTITION BY toDate(toDateTime(ts_ms/1000))
ORDER BY (creative_id, ifa, toDateTime(ts_ms/1000), event_type, sequence_index)
SETTINGS index_granularity = 8192;
