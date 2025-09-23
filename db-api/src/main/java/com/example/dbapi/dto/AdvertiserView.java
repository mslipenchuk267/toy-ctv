package com.example.dbapi.dto;

import org.springframework.data.annotation.Id;

public record AdvertiserView(@Id Integer id, String name) { }
