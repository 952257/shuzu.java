package com.springcloud.sakila.city.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springcloud.sakila.city.common.CommonResult;
import com.springcloud.sakila.city.common.ServiceException;
import com.springcloud.sakila.city.common.ServiceExceptionEnum;
import com.springcloud.sakila.city.dto.CityDto;
import com.springcloud.sakila.city.feign.Country;
import com.springcloud.sakila.city.feign.CountryFeignClient;
import com.springcloud.sakila.city.mapper.CityMapper;
import com.springcloud.sakila.city.po.City;
import com.springcloud.sakila.city.service.CityService;
import com.springcloud.sakila.city.vo.CityVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class CityServiceImpl implements CityService {

    @Resource
    private CityMapper cityMapper;

    @Resource
    private CountryFeignClient countryFeignClient;

    @Override
    @Transactional
    public void addOne(CityDto cityDto) {
        City po = new City();
        BeanUtils.copyProperties(cityDto, po);
        if (cityDto.getCountryId() == null) {
            Country country = new Country();
            country.setCountry(cityDto.getCountry());
            CommonResult<Country> result = countryFeignClient.addOne(country);
            if (result == null || result.getData() == null) {
                throw new ServiceException(ServiceExceptionEnum.SYS_ERROR);
            }
            po.setCountryId(result.getData().getCountryId());
        }
        cityMapper.insert(po);
    }

    @Override
    public void modOne(CityDto cityDto) {
        City po = new City();
        BeanUtils.copyProperties(cityDto, po);
        cityMapper.updateById(po);
    }

    @Override
    public void delOne(Integer id) {
        cityMapper.deleteById(id);
    }

    @Override
    public CityVo queryOne(Integer id) {
        City po = cityMapper.selectById(id);
        if (po == null) {
            throw new ServiceException(ServiceExceptionEnum.CITY_NOT_FOUND);
        }
        CityVo vo = new CityVo();
        BeanUtils.copyProperties(po, vo);
        fillCountry(vo);
        return vo;
    }

    @Override
    public Page<CityVo> pages(String city, int current, int size, String orderBy, String order) {
        Page<City> page = new Page<>(current, size);
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(orderBy);
        orderItem.setAsc("asc".equals(order));
        page.addOrder(orderItem);
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        if (city != null && !city.isEmpty()) {
            queryWrapper.like("city", city);
        }
        Page<City> cityPage = cityMapper.selectPage(page, queryWrapper);
        Page<CityVo> cityVoPage = new Page<>();
        cityVoPage.setPages(cityPage.getPages());
        cityVoPage.setCurrent(cityPage.getCurrent());
        cityVoPage.setSize(cityPage.getSize());
        cityVoPage.setTotal(cityPage.getTotal());
        cityVoPage.setRecords(cityPage.getRecords().stream().map(po -> {
            CityVo vo = new CityVo();
            BeanUtils.copyProperties(po, vo);
            fillCountry(vo);
            return vo;
        }).toList());
        return cityVoPage;
    }

    @Override
    public Page<CityVo> pages2(String city, String country, int current, int size, String orderBy, String order) {
        Page<City> page = new Page<>(current, size);
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(orderBy);
        orderItem.setAsc("asc".equals(order));
        page.addOrder(orderItem);
        return cityMapper.selectCityAndCountryByPage(city, country, page);
    }

    private void fillCountry(CityVo vo) {
        if (vo.getCountryId() == null) {
            return;
        }
        CommonResult<Country> result = countryFeignClient.queryById(vo.getCountryId());
        if (result != null && result.getData() != null) {
            vo.setCountry(result.getData().getCountry());
        }
    }
}
