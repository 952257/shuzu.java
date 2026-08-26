package com.springcloud.sakila.country.controller;

import com.springcloud.sakila.country.po.Country;
import com.springcloud.sakila.country.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("country")
@Slf4j
public class CountryController {

    @Resource
    private CountryService countryService;

    @GetMapping("{id}")
    public Country queryOne(@PathVariable Integer id) {
        log.info("query country id={}", id);
        return countryService.queryById(id);
    }

    @PostMapping
    public Country addOne(@RequestBody Country country) {
        log.info("add country {}", country);
        return countryService.addOne(country);
    }
}
