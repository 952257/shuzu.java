package com.springcloud.sakila.city.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springcloud.sakila.city.dto.CityDto;
import com.springcloud.sakila.city.service.CityService;
import com.springcloud.sakila.city.vo.CityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("city")
@Slf4j
public class CityController {

    @Resource
    private CityService cityService;

    @PostMapping
    public void addOne(@RequestBody CityDto cityDto) {
        log.info("cityDto is {}", cityDto);
        cityService.addOne(cityDto);
    }

    @PutMapping("{id}")
    public void modOne(@PathVariable Integer id, @RequestBody CityDto cityDto) {
        log.info("cityDto is {}", cityDto);
        cityDto.setCityId(id);
        cityService.modOne(cityDto);
    }

    @DeleteMapping("{id}")
    public void delOne(@PathVariable Integer id) {
        log.info("id is {}", id);
        cityService.delOne(id);
    }

    @GetMapping("{id}")
    public CityVo queryOne(@PathVariable Integer id) {
        return cityService.queryOne(id);
    }

    @GetMapping("/pages")
    public Page<CityVo> pages(@RequestParam(required = false) String city,
                              @RequestParam int current,
                              @RequestParam int size,
                              @RequestParam String orderBy,
                              @RequestParam String order) {
        return cityService.pages(city, current, size, orderBy, order);
    }

    @GetMapping("/pages2")
    public Page<CityVo> pages2(@RequestParam(required = false) String city,
                               @RequestParam(required = false) String country,
                               @RequestParam int current,
                               @RequestParam int size,
                               @RequestParam String orderBy,
                               @RequestParam String order) {
        return cityService.pages2(city, country, current, size, orderBy, order);
    }
}
