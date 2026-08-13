package com.sakila.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sakila.mybatisplus.po.Country;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface  CountryMapper extends BaseMapper<Country> {


}
