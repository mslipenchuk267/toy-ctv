package com.example.dbapi.dto;

import org.springframework.data.annotation.Id;

public record Advertiser(@Id Integer id, String name) { }
