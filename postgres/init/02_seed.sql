INSERT INTO advertisers(name) VALUES ('Acme Co.') ON CONFLICT DO NOTHING;

INSERT INTO campaigns(advertiser_id, name, status, start_date)
SELECT advertiser_id, 'Spring Promo', 'active', CURRENT_DATE
FROM advertisers WHERE name='Acme Co.'
ON CONFLICT DO NOTHING;

INSERT INTO creatives(creative_id_nat, campaign_id, name, duration_ms)
SELECT 'cr_demo_30', c.campaign_id, 'Demo 30s', 30000
FROM campaigns c JOIN advertisers a USING(advertiser_id)
WHERE a.name='Acme Co.' AND c.name='Spring Promo'
ON CONFLICT DO NOTHING;
