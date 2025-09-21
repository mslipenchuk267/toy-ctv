package com.example.dbapi;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertiserRepository extends ListCrudRepository<Advertiser, Integer> { }
