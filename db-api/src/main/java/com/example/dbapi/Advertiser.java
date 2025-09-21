package com.example.dbapi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "advertisers")
public class Advertiser {

    @Id
    @Column(name = "advertiser_id")
    private int id;
    private String name;

    protected Advertiser() {
        // JPA requires a no-args constructor
    }

    public Advertiser(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
