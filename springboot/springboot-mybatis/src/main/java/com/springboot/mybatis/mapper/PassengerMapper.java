package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Passenger;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PassengerMapper {
    // 嵌套 Select 查询：先查乘客，再按 id 查护照
	Passenger selectPassengerById(Integer id);

	// 嵌套结果映射：一次 join 查出乘客+护照
	Passenger selectPassengerById2(Integer id);

	// 查询所有乘客以及关联的护照信息（嵌套 Select）
	@Select("select * from t_passengers")
	@ResultMap("passengerAndPassportMap")
	List<Passenger> selectAllPassengers();

	// 只查询乘客基本数据，不查关联
	@Select("select * from t_passengers where id = #{id}")
	Passenger selectPassengerBaseById(Integer id);

}