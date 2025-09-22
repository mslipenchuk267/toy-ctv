package com.example.dbapi.creatives.dto;

public record CreativeCreate(String creativeIdNat, Integer campaignId,
                             String name, Integer durationMs) { }
