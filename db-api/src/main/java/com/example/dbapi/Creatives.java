package com.example.dbapi;

import jakarta.persistence.*;

@Entity
@Table(name = "creatives")
public class Creatives {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creative_sk")
    private int id;

    @Column(name = "creative_id_nat")
    private String creativeIdNat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaigns campaign;

    private String name;

    @Column(name = "duration_ms")
    private int durationMs;

    protected Creatives() {

    }

    public Creatives(String creative_id_nat, String name, Integer duration_ms,
                     Campaigns campaign) {
        this.creativeIdNat = creative_id_nat;
        this.name = name;
        this.durationMs = duration_ms;
        this.campaign = campaign;
    }

    public int getId() { return id; }

    public int getCampaignId() { return campaign.getId(); }

    public String getCreativeIdNat() { return creativeIdNat; }

    public String getName() { return name; }

    public int getDurationMs() { return durationMs; }
}
