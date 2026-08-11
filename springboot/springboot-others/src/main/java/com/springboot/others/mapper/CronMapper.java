package com.springboot.others.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface CronMapper {
    @Select("select cron from scheduled where id=#{id}")
    String getCron(Long id);
}