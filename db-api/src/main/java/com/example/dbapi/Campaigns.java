package com.example.dbapi;

import com.example.dbapi.dto.Advertiser;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "campaigns")
public class Campaigns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    private int id;
    //@Column(name = "advertiser_id")
    //private int advertiserId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "advertiser_id", nullable = false)
    private Advertisers advertiser;

    private String name;
    private String status;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    protected Campaigns() {}

    public Campaigns(String name, String status,
                     Date start_date, Date end_data, Advertisers advertiser) {
        this.name = name;
        this.status = status;
        this.startDate = start_date;
        this.endDate = end_data;
        this.advertiser = advertiser;

    }

    public int getId() { return id; }

    public int getAdvertiserId() { return advertiser.getId(); }

    public String getAdvertiserName() { return advertiser.getName(); }

    public String getName() { return name; }

    public String getStatus() {return status; }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() { return endDate; }
}
