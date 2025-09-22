package com.example.dbapi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class TransactionalService {
    private final AdvertiserRepository advRepo;
    private final CampaignRepository campRepo;
    private final CreativeRepository creaRepo;

    public TransactionalService(AdvertiserRepository repo,
                                CampaignRepository campRepo,
                                CreativeRepository creaRepo) {
        this.advRepo = repo;
        this.campRepo = campRepo;
        this.creaRepo = creaRepo;
    }

    @Transactional
    public Optional<Advertiser> getAdvertiserById(Integer id) {
        return advRepo.findById(id);
    }

    @Transactional
    public Optional<Campaign> getCampaignById(Integer id) {
        return campRepo.findById(id);
    }

    @Transactional
    public Optional<Creative> getCreativeById(Integer id) {
        return creaRepo.findById(id);
    }

    @Transactional
    public Collection<Creative> getCreativeByCampaign(Integer campaignId) {
        return creaRepo.findByCampaignId(campaignId);
    }

    @Transactional
    public Collection<Creative> getAllCreatives() {
        return creaRepo.findAll();
    }

}
