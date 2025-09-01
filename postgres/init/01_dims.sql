-- Basic Type-1 dims you’ll fill later via the Kotlin API
CREATE TABLE IF NOT EXISTS advertisers (
  advertiser_id SERIAL PRIMARY KEY,
  name         TEXT NOT NULL UNIQUE,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS campaigns (
  campaign_id   SERIAL PRIMARY KEY,
  advertiser_id INT NOT NULL REFERENCES advertisers(advertiser_id),
  name          TEXT NOT NULL,
  status        TEXT NOT NULL DEFAULT 'active',
  start_date    DATE,
  end_date      DATE,
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE(advertiser_id, name)
);

CREATE TABLE IF NOT EXISTS creatives (
  creative_sk     SERIAL PRIMARY KEY,
  creative_id_nat TEXT NOT NULL UNIQUE,
  campaign_id     INT NOT NULL REFERENCES campaigns(campaign_id),
  name            TEXT NOT NULL,
  duration_ms     INT  NOT NULL,
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
