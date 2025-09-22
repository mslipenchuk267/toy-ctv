package com.example.dbapi.dto;

import org.springframework.data.annotation.Id;

public record Creative(@Id Integer id, String name, Integer durationMs) {
}
