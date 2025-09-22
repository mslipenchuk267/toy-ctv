package com.example.dbapi;

import com.example.dbapi.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CatalogController {

    private final CatalogService catalogSrv;

    public CatalogController(CatalogService service){
        this.catalogSrv = service;
    }

    // POSTS ------------------------------------------------------------------
    @PostMapping("/advertisers")
    public ResponseEntity<Advertiser> createAdvertiser(@RequestBody AdvertiserCreate req,
                                                       UriComponentsBuilder uri) {
        var created = catalogSrv.createAdvertiser(req);
        var location = uri.path("/advertisers/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/campaigns")
    public ResponseEntity<Campaign> createCampaign(@RequestBody CampaignCreate req,
                                                   UriComponentsBuilder uri) {
        var created = catalogSrv.createCampaign(req);
        var location = uri.path("/campaigns/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // GETS -------------------------------------------------------------------
    @GetMapping("/advertisers")
    public Page<Advertiser> advertisers(
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return catalogSrv.getAllAdvertisers(pageable);
    }

    @GetMapping("/campaigns")
    public Page<Campaign> campaigns(
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return catalogSrv.getAllCampaigns(pageable);
    }

    @GetMapping("/advertisers/{id}")
    public Advertiser advertiser(@PathVariable Integer id) {
        return catalogSrv.getAdvertiserById(id);
    }

    @GetMapping("/campaigns/{id}")
    public Campaign campaign(@PathVariable Integer id) {
        return catalogSrv.getCampaignById(id);
    }

    @GetMapping("/creatives/{id}")
    public Creative creative(@PathVariable Integer id) {
        return catalogSrv.getCreativeById(id);
    }

    // Creative-related endpoints
    @GetMapping("/creatives")
    public Page<Creative> creatives(
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return catalogSrv.getAllCreatives(pageable);
    }

    @GetMapping("/campaigns/{id}/creatives")
    public Page<Creative> campaignCreatives(
            @PathVariable Integer id,
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return catalogSrv.getCreativeByCampaign(id, pageable);
    }


}
