package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Passport;
import org.apache.ibatis.annotations.Select;

public interface PassportMapper {

    @Select("select * from t_passports where passenger_id = #{id}")
    Passport selectPassportByPassengerId(Integer id);

    // 嵌套 Select 查询：先查护照，再按 passenger_id 查乘客
    Passport selectPassportById(Integer id);

    // 嵌套结果映射：一次 join 查出护照+乘客
    Passport selectPassportById2(Integer id);

}