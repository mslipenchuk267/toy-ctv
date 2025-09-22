package com.example.dbapi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.beans.ConstructorProperties;
import java.util.Date;

@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id
    @Column(name = "campaign_id")
    private int id;
    @Column(name = "advertiser_id")
    private int advertiserId;
    private String name;
    private String status;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    protected Campaign() {}

    public Campaign(int id, int advertiser_id,
                    String name, String status,
                    Date start_date, Date end_data) {
        this.id = id;
        this.advertiserId = advertiser_id;
        this.name = name;
        this.status = status;
        this.startDate = start_date;
        this.endDate = end_data;

    }

    public int getId() { return id; }

    public int getAdvertiserId() { return advertiserId; }

    public String getName() { return name; }

    public String getStatus() {return status; }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() { return endDate; }
}
