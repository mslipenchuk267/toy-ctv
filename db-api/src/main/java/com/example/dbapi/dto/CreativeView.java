package com.example.dbapi.dto;

import org.springframework.data.annotation.Id;

public record CreativeView(@Id Integer id, String name, Integer durationMs) {
}
