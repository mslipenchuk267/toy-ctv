package com.example.dbapi.creatives.dto;

import org.springframework.data.annotation.Id;

public record CreativeView(@Id Integer id, String name, Integer durationMs) {
}
