package com.example.dbapi;

import com.example.dbapi.advertisers.Advertiser;
import com.example.dbapi.advertisers.AdvertiserRepository;
import com.example.dbapi.advertisers.dto.AdvertiserCreate;
import com.example.dbapi.advertisers.dto.AdvertiserView;
import com.example.dbapi.campaigns.Campaign;
import com.example.dbapi.campaigns.CampaignRepository;
import com.example.dbapi.campaigns.dto.CampaignCreate;
import com.example.dbapi.campaigns.dto.CampaignView;
import com.example.dbapi.creatives.Creative;
import com.example.dbapi.creatives.CreativeRepository;
import com.example.dbapi.creatives.dto.CreativeCreate;
import com.example.dbapi.creatives.dto.CreativeView;
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
    public AdvertiserView createAdvertiser(AdvertiserCreate req) {
        var a = new Advertiser();
        a.setName(req.name());
        var saved = advertisers.save(a);
        return new AdvertiserView(saved.getId(), saved.getName());
    }

    @Transactional
    public CampaignView createCampaign(CampaignCreate req) {
        var a = orNotFound(advertisers.findById(req.advertiserId()), "Advertiser w/ id: " + req.advertiserId() + " not found.");
        var c = new Campaign(
                req.name(), req.status(), req.startDate(), req.endDate(),
                a);
        var saved = campaigns.save(c);
        return new CampaignView(
                saved.getId(), saved.getName(), saved.getStatus(),
                saved.getStartDate(), saved.getEndDate()
        );
    }

    @Transactional
    public CreativeView createCreative(CreativeCreate req) {
        var c = orNotFound(campaigns.findById(req.campaignId()), "Campaign w/ id: " + req.campaignId() + " not found.");
        var cr = new Creative(
                req.creativeIdNat(), req.name(), req.durationMs(),
                c
        );
        var saved = creatives.save(cr);
        return new CreativeView(
                saved.getId(), saved.getName(), saved.getDurationMs()
        );
    }

    // Single-Entity reads ----------------------------------------------------
    @Transactional(readOnly = true)
    public AdvertiserView getAdvertiserById(Integer id) {
        return orNotFound(advertisers.findById(id, AdvertiserView.class), "Advertiser w/ id: " + id + " not found.");
    }

    @Transactional(readOnly = true)
    public CampaignView getCampaignById(Integer id) {
        return orNotFound(campaigns.findById(id, CampaignView.class), "Campaign w/ id: " + id + " not found.");
    }

    @Transactional(readOnly = true)
    public CreativeView getCreativeById(Integer id) {
        return orNotFound(creatives.findById(id, CreativeView.class), "Creative w/ id: " + id + " not found.");
    }

    // Multi-Entity Reads (paged) ---------------------------------------------
    @Transactional(readOnly = true)
    public Page<AdvertiserView> getAllAdvertisers(Pageable pageable) {
        return advertisers.findAllBy(pageable, AdvertiserView.class);
    }

    @Transactional
    public Page<CampaignView> getAllCampaigns(Pageable pageable) {
        return campaigns.findAllBy(pageable, CampaignView.class);
    }

    @Transactional(readOnly = true)
    public Page<CreativeView> getCreativesByCampaign(Integer campaignId, Pageable pageable) {
        return creatives.findByCampaignId(campaignId, pageable, CreativeView.class);
    }

    @Transactional(readOnly = true)
    public Page<CreativeView> getAllCreatives(Pageable pageable) {
        return creatives.findAllBy(pageable, CreativeView.class);
    }

}
