package com.example.dbapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class CatalogController {

    private final CatalogService catalogSrv;

    public CatalogController(CatalogService service){
        this.catalogSrv = service;
    }

    @GetMapping("/advertiser")
    public Advertiser advertiser(@RequestParam Integer id) {
        return catalogSrv.getAdvertiserById(id);
    }

    @GetMapping("/campaign")
    public Campaign campaign(@RequestParam Integer id) {
        return catalogSrv.getCampaignById(id);
    }

    // Creative-related endpoints
    @GetMapping("/creatives")
    public Page<Creative> creatives(
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return catalogSrv.getAllCreatives(pageable);
    }

    @GetMapping("/campaign/{id}/creatives")
    public Page<Creative> campaignCreatives(
            @PathVariable Integer id,
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return catalogSrv.getCreativeByCampaign(id, pageable);
    }

    @GetMapping("/creative")
    public Creative creative(@RequestParam Integer id) {
        return catalogSrv.getCreativeById(id);
    }
}
