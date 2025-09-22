package com.example.dbapi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "creatives")
public class Creative {

    @Id
    @Column(name = "creative_sk")
    private int id;

    @Column(name = "creative_id_nat")
    private String creativeIdNat;

    @Column(name = "campaign_id")
    private int campaignId;

    private String name;

    @Column(name = "duration_ms")
    private int durationMs;

    protected Creative() {

    }

    public Creative(int id, String creative_id_nat, Integer campaign_id, String name, Integer duration_ms) {
        this.id = id;
        this.creativeIdNat = creative_id_nat;
        this.campaignId = campaign_id;
        this.name = name;
        this.durationMs = duration_ms;
    }

    public int getId() { return id; }

    public int getCampaignId() { return campaignId; }

    public String getCreativeIdNat() { return creativeIdNat; }

    public String getName() { return name; }

    public int getDurationMs() { return durationMs; }
}
