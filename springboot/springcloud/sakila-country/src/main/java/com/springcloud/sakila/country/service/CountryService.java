package com.springcloud.sakila.country.service;

import com.springcloud.sakila.country.po.Country;

public interface CountryService {
    Country queryById(Integer id);

    Country addOne(Country country);
}
