package com.example.dbapi;

import com.example.dbapi.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class CatalogService {
    private final AdvertiserRepository advertisers;
    private final CampaignRepository campaigns;
    private final CreativeRepository creatives;

    public CatalogService(AdvertiserRepository repo,
                          CampaignRepository campaigns,
                          CreativeRepository creatives) {
        this.advertisers = repo;
        this.campaigns = campaigns;
        this.creatives = creatives;
    }

    // Helpers ----------------------------------------------------------------
    public static <T> T orNotFound(Optional<T> opt, String what) {
        return opt.orElseThrow(() -> new EntityNotFoundException(what));
    }

    // Writes -----------------------------------------------------------------
    @Transactional
    public Advertiser createAdvertiser(AdvertiserCreate req) {
        var a = new Advertisers();
        a.setName(req.name());
        var saved = advertisers.save(a);
        return new Advertiser(saved.getId(), saved.getName());
    }

    @Transactional
    public Campaign createCampaign(CampaignCreate req) {
        var a = orNotFound(advertisers.findById(req.advertiserId()), "Advertiser w/ id: " + req.advertiserId() + " not found.");
        var c = new Campaigns(
                req.name(), req.status(), req.startDate(), req.endDate(),
                a);
        var saved = campaigns.save(c);
        return new Campaign(
                saved.getId(), saved.getName(), saved.getStatus(),
                saved.getStartDate(), saved.getEndDate()
        );
    }

    @Transactional
    public Creative createCreative(CreativeCreate req) {
        var c = orNotFound(campaigns.findById(req.campaignId()), "Campaign w/ id: " + req.campaignId() + " not found.");
        var cr = new Creatives(
                req.creativeIdNat(), req.name(), req.durationMs(),
                c
        );
        var saved = creatives.save(cr);
        return new Creative(
                saved.getId(), saved.getName(), saved.getDurationMs()
        );
    }

    // Single-Entity reads ----------------------------------------------------
    @Transactional(readOnly = true)
    public Advertiser getAdvertiserById(Integer id) {
        return orNotFound(advertisers.findById(id, Advertiser.class), "Advertiser w/ id: " + id + " not found.");
    }

    @Transactional(readOnly = true)
    public Campaign getCampaignById(Integer id) {
        return orNotFound(campaigns.findById(id, Campaign.class), "Campaign w/ id: " + id + " not found.");
    }

    @Transactional(readOnly = true)
    public Creative getCreativeById(Integer id) {
        return orNotFound(creatives.findById(id, Creative.class), "Creative w/ id: " + id + " not found.");
    }

    // Multi-Entity Reads (paged) ---------------------------------------------
    @Transactional(readOnly = true)
    public Page<Advertiser> getAllAdvertisers(Pageable pageable) {
        return advertisers.findAllBy(pageable, Advertiser.class);
    }

    @Transactional
    public Page<Campaign> getAllCampaigns(Pageable pageable) {
        return campaigns.findAllBy(pageable, Campaign.class);
    }

    @Transactional(readOnly = true)
    public Page<Creative> getCreativeByCampaign(Integer campaignId, Pageable pageable) {
        return creatives.findByCampaignId(campaignId, pageable, Creative.class);
    }

    @Transactional(readOnly = true)
    public Page<Creative> getAllCreatives(Pageable pageable) {
        return creatives.findAllBy(pageable, Creative.class);
    }

}
