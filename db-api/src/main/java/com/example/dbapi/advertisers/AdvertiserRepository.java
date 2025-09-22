package com.example.dbapi.advertisers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertiserRepository extends JpaRepository<Advertiser, Integer> {
    <T> Optional<T> findById(Integer id, Class<T> type);
    <T> Page<T> findAllBy(Pageable pageable, Class<T> type);
}
