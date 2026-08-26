package com.springcloud.sakila.country.service.impl;

import com.springcloud.sakila.country.common.ServiceException;
import com.springcloud.sakila.country.common.ServiceExceptionEnum;
import com.springcloud.sakila.country.mapper.CountryMapper;
import com.springcloud.sakila.country.po.Country;
import com.springcloud.sakila.country.service.CountryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CountryServiceImpl implements CountryService {

    @Resource
    private CountryMapper countryMapper;

    @Override
    public Country queryById(Integer id) {
        Country country = countryMapper.selectById(id);
        if (country == null) {
            throw new ServiceException(ServiceExceptionEnum.COUNTRY_NOT_FOUND);
        }
        return country;
    }

    @Override
    public Country addOne(Country country) {
        countryMapper.insert(country);
        return country;
    }
}
