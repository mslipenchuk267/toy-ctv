package com.example.dbapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

public interface CreativeRepository extends ListCrudRepository<Creative, Integer> {
    Page<Creative> findByCampaignId(Integer campaignId,
                                    Pageable pageable);

    Page<Creative> findAll(Pageable pageable);
}
