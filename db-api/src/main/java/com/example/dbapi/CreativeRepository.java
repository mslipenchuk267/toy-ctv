package com.example.dbapi;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;

public interface CreativeRepository extends ListCrudRepository<Creative, Integer> {
    Collection<Creative> findByCampaignId(Integer campaignId);
}
