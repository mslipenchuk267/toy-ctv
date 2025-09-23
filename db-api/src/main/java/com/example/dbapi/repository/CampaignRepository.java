package com.example.dbapi.repository;

import com.example.dbapi.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
    <T> Optional<T> findById(Integer id, Class<T> type);
    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);
}
