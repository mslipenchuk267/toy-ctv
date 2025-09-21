package com.example.dbapi;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AdvertiserController {

    private AdvertiserService advServ;

    public AdvertiserController(AdvertiserService service){
        this.advServ = service;
    }

    @GetMapping("/advertiser")
    public Advertiser advertiser(@RequestParam Integer id) {
        return advServ.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertiser with id " + id + " not found"));
    }
}
