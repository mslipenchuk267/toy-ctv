package com.example.dbapi.campaigns.dto;

import org.springframework.data.annotation.Id;

import java.util.Date;

public record CampaignView(@Id Integer id, String name, String Status,
                           Date startDate, Date endDate) { }
