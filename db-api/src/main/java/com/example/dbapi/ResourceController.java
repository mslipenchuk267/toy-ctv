package com.example.dbapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ResourceController {

    @GetMapping("/advertiser")
    public Advertiser advertiser(@RequestParam Integer id, @RequestParam String name) {
        return new Advertiser(id, name);
    }

}
