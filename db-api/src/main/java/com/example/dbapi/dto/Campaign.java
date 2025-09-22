package com.example.dbapi.dto;

import org.springframework.data.annotation.Id;

import java.util.Date;

public record Campaign(@Id Integer id, String name, String Status,
                       Date startDate, Date endDate) { }
