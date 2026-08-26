package com.springcloud.sakila.city.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springcloud.sakila.city.dto.CityDto;
import com.springcloud.sakila.city.vo.CityVo;

public interface CityService {
    void addOne(CityDto cityDto);
    void modOne(CityDto cityDto);
    void delOne(Integer id);
    CityVo queryOne(Integer id);
    Page<CityVo> pages(String city, int current, int size, String orderBy, String order);
    Page<CityVo> pages2(String city, String country, int current, int size, String orderBy, String order);
}
