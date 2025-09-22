package com.example.dbapi;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class CatalogService {
    private final AdvertiserRepository advRepo;
    private final CampaignRepository campRepo;
    private final CreativeRepository creaRepo;

    public CatalogService(AdvertiserRepository repo,
                          CampaignRepository campRepo,
                          CreativeRepository creaRepo) {
        this.advRepo = repo;
        this.campRepo = campRepo;
        this.creaRepo = creaRepo;
    }

    // Helpers ----------------------------------------------------------------
    public static <T> T orNotFound(Optional<T> opt, String what) {
        return opt.orElseThrow(() -> new EntityNotFoundException(what));
    }

    // Single-Entity reads ----------------------------------------------------
    @Transactional
    public Advertiser getAdvertiserById(Integer id) {
        return orNotFound(advRepo.findById(id), "Advertiser w/ id: " + id + " not found.");
    }

    @Transactional
    public Campaign getCampaignById(Integer id) {
        return orNotFound(campRepo.findById(id), "Campaign w/ id: " + id + " not found.");
    }

    @Transactional
    public Creative getCreativeById(Integer id) {
        return orNotFound(creaRepo.findById(id), "Creative w/ id: " + id + " not found.");
    }

    // Multi-Entity Reads (paged) ---------------------------------------------
    @Transactional
    public Page<Creative> getCreativeByCampaign(Integer campaignId, Pageable pageable) {
        return creaRepo.findByCampaignId(campaignId, pageable);
    }

    @Transactional
    public Page<Creative> getAllCreatives(Pageable pageable) {
        return creaRepo.findAll(pageable);
    }

}
