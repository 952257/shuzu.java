package com.springcloud.sakila.city.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springcloud.sakila.city.po.City;
import com.springcloud.sakila.city.vo.CityVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper extends BaseMapper<City> {
    Page<CityVo> selectCityAndCountryByPage(String city, String country, Page<City> page);
}
