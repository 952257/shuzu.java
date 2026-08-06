package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Employee;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {

    @Select("SELECT * FROM t_employees WHERE dept_id = #{id}")
    List<Employee> selectEmployeeByDepartmentId(Integer id);
}