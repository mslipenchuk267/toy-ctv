package com.example.dbapi.repository;

import com.example.dbapi.model.Creative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreativeRepository extends JpaRepository<Creative, Integer> {
    <T> Optional<T> findById(Integer id, Class<T> type);

    <T> Page<T> findByCampaignId(Integer campaignId,
                             Pageable pageable,
                             Class<T> type);

    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);
}
