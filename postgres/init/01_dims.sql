-- DDL for basic Type-1 dims + utility triggers
CREATE TABLE IF NOT EXISTS advertisers (
  advertiser_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  name         TEXT NOT NULL UNIQUE,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS campaigns (
  campaign_id   INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  advertiser_id INT NOT NULL REFERENCES advertisers(advertiser_id),
  name          TEXT NOT NULL,
  status        TEXT NOT NULL DEFAULT 'active',
  start_date    DATE,
  end_date      DATE,
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE(advertiser_id, name)
);

CREATE TABLE IF NOT EXISTS creatives (
  creative_sk     INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  creative_id_nat TEXT NOT NULL UNIQUE,
  campaign_id     INT NOT NULL REFERENCES campaigns(campaign_id),
  name            TEXT NOT NULL,
  duration_ms     INT  NOT NULL,
  updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Useful indexes for joins from creatives -> campaigns -> advertisers
CREATE INDEX IF NOT EXISTS idx_campaigns_advertiser_id ON campaigns(advertiser_id);
CREATE INDEX IF NOT EXISTS idx_creatives_campaign_id   ON creatives(campaign_id);

-- Keep updated_at fresh
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
  NEW.updated_at := now();
  RETURN NEW;
END; $$;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname='trg_advertisers_updated_at') THEN
    CREATE TRIGGER trg_advertisers_updated_at BEFORE UPDATE ON advertisers
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
  END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname='trg_campaigns_updated_at') THEN
    CREATE TRIGGER trg_campaigns_updated_at BEFORE UPDATE ON campaigns
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
  END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname='trg_creatives_updated_at') THEN
    CREATE TRIGGER trg_creatives_updated_at BEFORE UPDATE ON creatives
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
  END IF;
END $$;

-- (Optional) make sure the app user can create publications/slots (superuser in Docker, but harmless):
ALTER ROLE app WITH REPLICATION;
