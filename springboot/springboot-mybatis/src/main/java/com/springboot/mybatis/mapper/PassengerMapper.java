package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Passenger;

public interface PassengerMapper {
    //嵌套结果映射
	Passenger selectPassengerById(Integer id);
	//嵌套Select查询
	Passenger selectPassengerById2(Integer id);
}