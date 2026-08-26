package com.springcloud.sakila.country.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springcloud.sakila.country.po.Country;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryMapper extends BaseMapper<Country> {
}
