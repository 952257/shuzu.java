package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.UserMyBatis;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMyBatisMapper {

    @Select("SELECT * FROM USERMYBATIS WHERE NAME = #{name}")
    UserMyBatis findByName(@Param("name") String name);

    @Insert("INSERT INTO USERMYBATIS(NAME, AGE) VALUES(#{name}, #{age})")
    int insert(@Param("name") String name, @Param("age") Integer age);

    @Select("select * from usermybatis where id = #{id}")
    UserMyBatis selectById(Long id);
}