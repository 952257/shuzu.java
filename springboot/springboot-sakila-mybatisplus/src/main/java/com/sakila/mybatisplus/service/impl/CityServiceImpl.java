package com.sakila.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sakila.mybatisplus.dto.CityDto;
import com.sakila.mybatisplus.mapper.CityMapper;
import com.sakila.mybatisplus.mapper.CountryMapper;
import com.sakila.mybatisplus.po.City;
import com.sakila.mybatisplus.po.Country;
import com.sakila.mybatisplus.service.CityService;
import com.sakila.mybatisplus.vo.CityVo;
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
    private CountryMapper countryMapper;

    @Override
    @Transactional
    public void addOne(CityDto cityDto) {
        City po = new City();
        BeanUtils.copyProperties(cityDto, po);
        //判断国家不存在
        if(cityDto.getCountryId() == null) {
            Country country = new Country();
            country.setCountry(cityDto.getCountry());
            countryMapper.insert(country);
            po.setCountryId(country.getCountryId());
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
//        City po = cityMapper.selectById(id);
        return cityMapper.selectCityAndCountryById(id);
    }

    @Override
    public Page<CityVo> pages(String city, int current, int size, String orderBy, String order) {
        Page<City> page = new Page<>(current, size);
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(orderBy);
        orderItem.setAsc("asc".equals(order));
        page.addOrder(orderItem);
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        if(city != null && !city.isEmpty()){
            queryWrapper.like("city", city);
        }
        Page<City> cityPage = cityMapper.selectPage(page, queryWrapper);
        Page<CityVo> cityVoPage = new Page<>();
        cityVoPage.setPages(cityPage.getPages());
        cityVoPage.setCurrent(cityPage.getCurrent());
        cityVoPage.setSize(cityPage.getSize());
        cityVoPage.setTotal(cityPage.getTotal());
        cityVoPage.setRecords(cityPage.getRecords().stream().map(po->{
            CityVo vo = new CityVo();
            BeanUtils.copyProperties(po, vo);
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
}
