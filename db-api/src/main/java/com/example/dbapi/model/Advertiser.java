package com.example.dbapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "advertisers")
public class Advertiser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertiser_id")
    private int id;
    private String name;

    public Advertiser() {
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

    public void setId(Integer id) { this.id = id; }

    public void setName(String name) { this.name = name; }
}
