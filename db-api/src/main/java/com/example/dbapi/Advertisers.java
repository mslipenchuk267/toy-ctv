package com.example.dbapi;

import jakarta.persistence.*;

@Entity
@Table(name = "advertisers")
public class Advertisers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertiser_id")
    private int id;
    private String name;

    protected Advertisers() {
        // JPA requires a no-args constructor
    }

    public Advertisers(int id, String name) {
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
