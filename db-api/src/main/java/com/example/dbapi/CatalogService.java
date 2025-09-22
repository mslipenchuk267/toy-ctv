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
    private final CreativeRepository creaRepo;

    public CatalogService(AdvertiserRepository repo,
                          CampaignRepository campaigns,
                          CreativeRepository creaRepo) {
        this.advertisers = repo;
        this.campaigns = campaigns;
        this.creaRepo = creaRepo;
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
        return orNotFound(creaRepo.findById(id, Creative.class), "Creative w/ id: " + id + " not found.");
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
        return creaRepo.findByCampaignId(campaignId, pageable, Creative.class);
    }

    @Transactional(readOnly = true)
    public Page<Creative> getAllCreatives(Pageable pageable) {
        return creaRepo.findAllBy(pageable, Creative.class);
    }

}
