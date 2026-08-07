package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Passenger;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PassengerMapper {
	//嵌套结果映射
	Passenger selectPassengerById(Integer id);


	//嵌套Select查询
	Passenger selectPassengerById2(Integer id);

	//嵌套结果映射 查询所有乘客以及关联的护照信息
	@Select("select * from t_passengers")
	@ResultMap("passengerAndPassportMap")
	List<Passenger> selectAllPassengers();

	//只查询乘客的基本数据，不查询关联的数据
	@Select("select * from t_passengers where id = #{id}")
	Passenger selectPassengerBaseById(Integer id);

	//条件查询
	List<Passenger> selectPassengerByCondition(Passenger passenger);

	List<Passenger> selectPassengerByCondition2(Passenger passenger);


	void updatePassengerIfNecessary(Passenger passenger);

	List<Passenger> selectPassengersIn(Integer... ids);
}