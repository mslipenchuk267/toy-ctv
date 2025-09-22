package com.example.dbapi.campaigns.dto;

import java.util.Date;

public record CampaignCreate(Integer advertiserId,
                             String name, String status,
                             Date startDate, Date endDate) { }
