package com.example.dbapi;

import com.example.dbapi.advertisers.dto.AdvertiserCreate;
import com.example.dbapi.advertisers.dto.AdvertiserView;
import com.example.dbapi.campaigns.dto.CampaignCreate;
import com.example.dbapi.campaigns.dto.CampaignView;
import com.example.dbapi.creatives.dto.CreativeCreate;
import com.example.dbapi.creatives.dto.CreativeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CatalogController {

    private final CatalogService catalogSrv;

    public CatalogController(CatalogService service){
        this.catalogSrv = service;
    }

    // POSTS ------------------------------------------------------------------
    @PostMapping("/advertisers")
    public ResponseEntity<AdvertiserView> createAdvertiser(@RequestBody AdvertiserCreate req,
                                                           UriComponentsBuilder uri) {
        var created = catalogSrv.createAdvertiser(req);
        var location = uri.path("/advertisers/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/campaigns")
    public ResponseEntity<CampaignView> createCampaign(@RequestBody CampaignCreate req,
                                                       UriComponentsBuilder uri) {
        var created = catalogSrv.createCampaign(req);
        var location = uri.path("/campaigns/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/creatives")
    public ResponseEntity<CreativeView> createCreative(@RequestBody CreativeCreate req,
                                                       UriComponentsBuilder uri) {
        var created = catalogSrv.createCreative(req);
        var location = uri.path("/creatives/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // GETS -------------------------------------------------------------------
    // All paginated
    @GetMapping("/advertisers")
    public Page<AdvertiserView> advertisers(
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return catalogSrv.getAllAdvertisers(pageable);
    }

    @GetMapping("/campaigns")
    public Page<CampaignView> campaigns(
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return catalogSrv.getAllCampaigns(pageable);
    }

    @GetMapping("/creatives")
    public Page<CreativeView> creatives(
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return catalogSrv.getAllCreatives(pageable);
    }

    // Single Response on Primary Key
    @GetMapping("/advertisers/{id}")
    public AdvertiserView advertiser(@PathVariable Integer id) {
        return catalogSrv.getAdvertiserById(id);
    }

    @GetMapping("/campaigns/{id}")
    public CampaignView campaign(@PathVariable Integer id) {
        return catalogSrv.getCampaignById(id);
    }

    @GetMapping("/creatives/{id}")
    public CreativeView creative(@PathVariable Integer id) {
        return catalogSrv.getCreativeById(id);
    }

    // Creatives for Campaign
    @GetMapping("/campaigns/{id}/creatives")
    public Page<CreativeView> campaignCreatives(
            @PathVariable Integer id,
            @PageableDefault(size = 25, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return catalogSrv.getCreativesByCampaign(id, pageable);
    }


}
