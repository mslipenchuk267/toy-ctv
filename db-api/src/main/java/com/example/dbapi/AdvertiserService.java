package com.example.dbapi;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdvertiserService {
    private final AdvertiserRepository advRepo;

    public AdvertiserService(AdvertiserRepository repo) {
        this.advRepo = repo;
    }

    public Optional<Advertiser> getById(Integer id) {
        return advRepo.findById(id);
    }
}
