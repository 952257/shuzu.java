package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Department;

public interface DepartmentMapper {
    Department selectDepartmentById(Integer id);

    Department selectDepartmentById2(Integer id);
}