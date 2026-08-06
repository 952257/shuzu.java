package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Passport;
import org.apache.ibatis.annotations.Select;

public interface PassportMapper {

    @Select("select * from t_passports where passenger_id = #{id}")
    Passport selectPassportByPassengerId(Integer id);


//    @Select("select * from t_passports where id = #{id}")
    Passport selectPassportById(Integer id);

    Passport selectPassportById2(Integer id);


}