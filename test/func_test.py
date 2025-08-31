# test_runner.py
import argparse
import random
import time
import uuid
from datetime import datetime, timezone
from typing import Dict, Any, List

from ctv_producer import KafkaJSONProducer

# -------------------------
# Raw-like catalogs (STABLE across runs)
# Only fields a TV/app/player would normally know/emit.
# -------------------------

DEVICES = [
    # ifa_type examples: "RIDA" (Roku), "AAID" (Android TV), "TIFA" (Tizen), "LGADID" (webOS)
    {"device_make": "Roku",    "device_model": "4802X", "device_os": "RokuOS", "device_os_version": "12.5",
     "ifa_type": "RIDA",  "ifa": "rida-1111-2222-3333", "limit_ad_tracking": False, "ua": "Roku/DVP-12.5 (4802X)"},
    {"device_make": "Samsung", "device_model": "QN90B", "device_os": "Tizen",  "device_os_version": "7.0",
     "ifa_type": "TIFA",  "ifa": "tifa-aaaa-bbbb-cccc", "limit_ad_tracking": False, "ua": "Mozilla/5.0 (Tizen 7.0)"},
    {"device_make": "LG",      "device_model": "CX",    "device_os": "webOS",  "device_os_version": "6.2",
     "ifa_type": "LGADID","ifa": "lgad-0000-1111-2222", "limit_ad_tracking": False, "ua": "Mozilla/5.0 (Web0S.TV-6.2)"},
    {"device_make": "TCL",     "device_model": "R646",  "device_os": "Android TV", "device_os_version": "13",
     "ifa_type": "AAID",  "ifa": "aaid-4444-5555-6666", "limit_ad_tracking": False, "ua": "AndroidTV/13 (TCL R646)"},
]

APPS = [
    {"app_bundle": "tv.tubi.app",   "app_name": "Tubi",   "app_version": "4.17.0", "player_name": "ExoPlayer", "player_version": "2.19.1"},
    {"app_bundle": "tv.plex.app",   "app_name": "Plex",   "app_version": "9.8.1",  "player_name": "ExoPlayer", "player_version": "2.18.7"},
    {"app_bundle": "tv.pluto.app",  "app_name": "Pluto",  "app_version": "5.39.2", "player_name": "AVPlayer",  "player_version": "1.0.0"},
]

# FAST-like channels/content (strings are raw; no joins needed)
CHANNELS = [
    {"channel_name": "News Daily",   "content_id": "prog_news_daily",  "content_title": "Hourly News",    "is_live": True},
    {"channel_name": "Foodie TV",    "content_id": "prog_food_daily",  "content_title": "Kitchen Stories","is_live": True},
    {"channel_name": "Travel Live",  "content_id": "prog_travel_live", "content_title": "City Hoppers",   "is_live": True},
]

# Stable creatives an ad SDK might reference (no campaign/advertiser joins)
CREATIVES = [
    {"creative_id": "cr_15_hike_more",    "ad_duration_ms": 15_000, "skippable": False, "skip_offset_ms": None},
    {"creative_id": "cr_30_pack_light",   "ad_duration_ms": 30_000, "skippable": True,  "skip_offset_ms": 5_000},
    {"creative_id": "cr_15_trail_grip",   "ad_duration_ms": 15_000, "skippable": False, "skip_offset_ms": None},
    {"creative_id": "cr_30_pour_over",    "ad_duration_ms": 30_000, "skippable": True,  "skip_offset_ms": 6_000},
]

VAST_EVENTS = [
    "ad_impression",
    "ad_video_start",
    "ad_first_quartile",
    "ad_midpoint",
    "ad_third_quartile",
    "ad_complete",
]

def now_iso() -> str:
    return datetime.now(timezone.utc).isoformat()

def gen_pod_records(
    device: Dict[str, Any],
    app: Dict[str, Any],
    channel: Dict[str, Any],
    creatives: List[Dict[str, Any]],
    base_ts_ms: int,
    rng: random.Random,
) -> List[Dict[str, Any]]:
    session_id = str(uuid.uuid4())
    ad_pod_id = str(uuid.uuid4())
    pod_len = len(creatives)

    order = creatives[:]
    rng.shuffle(order)

    playhead_ms = 0
    seq_index = 0
    records: List[Dict[str, Any]] = []

    for pos, cr in enumerate(order, start=1):
        # ad_id is a per-serving identifier (distinct from stable creative_id)
        ad_id = str(uuid.uuid4())
        dur = cr["ad_duration_ms"]

        for ev in VAST_EVENTS:
            seq_index += 1
            ts_ms = base_ts_ms + playhead_ms

            rec = {
                # --- event meta ---
                "schema_version": "1.0",
                "event_type": ev,
                "event_id": str(uuid.uuid4()),
                "ts_ms": ts_ms,
                "ingested_at": now_iso(),
                "sequence_index": seq_index,

                # --- session/pod ---
                "session_id": session_id,
                "ad_pod_id": ad_pod_id,
                "ad_pod_position": pos,    # 1..pod_len
                "ad_pod_len": pod_len,

                # --- device/app/player (raw) ---
                "device_make": device["device_make"],
                "device_model": device["device_model"],
                "device_os": device["device_os"],
                "device_os_version": device["device_os_version"],
                "ifa_type": device["ifa_type"],
                "ifa": device["ifa"],
                "limit_ad_tracking": device["limit_ad_tracking"],
                "user_agent": device["ua"],

                "app_bundle": app["app_bundle"],
                "app_name": app["app_name"],
                "app_version": app["app_version"],
                "player_name": app["player_name"],
                "player_version": app["player_version"],

                # --- content (raw strings) ---
                "channel_name": channel["channel_name"],
                "content_id": channel["content_id"],
                "content_title": channel["content_title"],
                "is_live": channel["is_live"],

                # --- ad/creative (no campaign/advertiser joins) ---
                "ad_id": ad_id,
                "creative_id": cr["creative_id"],
                "ad_duration_ms": cr["ad_duration_ms"],
                "ad_skippable": cr["skippable"],
                "ad_skip_offset_ms": cr["skip_offset_ms"],

                # --- playback flags you'd commonly get from the player ---
                "autoplay": rng.random() < 0.6,
                "fullscreen": True,
                "muted": rng.random() < 0.1,
                "playhead_ms": playhead_ms,
            }
            records.append(rec)

            # advance playhead within the ad for next beacon
            if ev == "ad_video_start":
                playhead_ms += int(0.05 * dur)
            elif ev == "ad_first_quartile":
                playhead_ms += int(0.25 * dur)
            elif ev == "ad_midpoint":
                playhead_ms += int(0.25 * dur)
            elif ev == "ad_third_quartile":
                playhead_ms += int(0.24 * dur)
            elif ev == "ad_complete":
                playhead_ms += int(0.21 * dur)

        # small gap between ads in the pod
        playhead_ms += rng.randint(250, 2000)

    return records


def main():
    p = argparse.ArgumentParser(description="Raw CTV beacon generator → Kafka")
    p.add_argument("--broker", default="localhost:9092")
    p.add_argument("--topic", default="dev.ctv.ad-beacons.raw.v1")
    p.add_argument("--sessions", type=int, default=10, help="Ad-pod sessions to simulate")
    p.add_argument("--min_ads_per_pod", type=int, default=1)
    p.add_argument("--max_ads_per_pod", type=int, default=3)
    p.add_argument("--stream_delay_ms", type=int, default=0, help="Delay between records")
    p.add_argument("--seed", type=int, default=1997, help="Optional RNG seed for deterministic runs")
    args = p.parse_args()

    if args.min_ads_per_pod < 1 or args.max_ads_per_pod < args.min_ads_per_pod:
        raise SystemExit("Invalid ads-per-pod range")

    rng = random.Random(args.seed)
    producer = KafkaJSONProducer(broker=args.broker, topic=args.topic)

    all_devices = DEVICES
    produced = 0

    for _ in range(args.sessions):
        device = rng.choice(all_devices)
        app = rng.choice(APPS)
        channel = rng.choice(CHANNELS)
        ad_count = rng.randint(args.min_ads_per_pod, args.max_ads_per_pod)
        chosen_creatives = rng.choices(CREATIVES, k=ad_count)

        base_ts_ms = int(time.time() * 1000) + rng.randint(0, 30_000)
        records = gen_pod_records(
            device=device,
            app=app,
            channel=channel,
            creatives=chosen_creatives,
            base_ts_ms=base_ts_ms,
            rng=rng,
        )

        for rec in records:
            # Use IFA as key (nice for partition locality and linkage across runs)
            producer.produce(rec, key=rec["ifa"])
            produced += 1
            if args.stream_delay_ms > 0:
                time.sleep(args.stream_delay_ms / 1000.0)

    producer.flush()
    print(f"Produced {produced} records → {args.topic} @ {args.broker}")


if __name__ == "__main__":
    main()
