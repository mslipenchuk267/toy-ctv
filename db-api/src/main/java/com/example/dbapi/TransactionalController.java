package com.example.dbapi;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RestController
public class TransactionalController {

    private final TransactionalService transServ;

    public TransactionalController(TransactionalService service){
        this.transServ = service;
    }

    @GetMapping("/advertiser")
    public Advertiser advertiser(@RequestParam Integer id) {
        return transServ.getAdvertiserById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertiser with id " + id + " not found"));
    }

    @GetMapping("/campaign")
    public Campaign campaign(@RequestParam Integer id) {
        return transServ.getCampaignById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaign with id " + id + " not found"));
    }

    // Creative-related endpoints
    @GetMapping("/creatives")
    public Collection<Creative> creatives() {
        return transServ.getAllCreatives();
    }

    @GetMapping("/campaign/{id}/creatives")
    public Collection<Creative> campaignCreatives(@PathVariable Integer id) {
        return transServ.getCreativeByCampaign(id);
    }

    @GetMapping("/creative")
    public Creative creative(@RequestParam Integer id) {
        return transServ.getCreativeById(id)
                .orElseThrow(() -> new EntityNotFoundException("Creative with id " + id + " not found"));
    }
}
