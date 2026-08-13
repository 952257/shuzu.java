package com.sakila.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sakila.mybatisplus.po.City;
import com.sakila.mybatisplus.vo.CityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CityMapper extends BaseMapper<City>{

    @Select("SELECT c.city_id, c.city, c.country_id, c.last_update, co.country " +
            "FROM city c " +
            "left JOIN country co ON c.country_id = co.country_id " +
            "WHERE c.city_id = #{cityId}")
    CityVo selectCityAndCountryById(Integer cityId);


    Page<CityVo> selectCityAndCountryByPage(String city, String country,Page<City> page);

}
