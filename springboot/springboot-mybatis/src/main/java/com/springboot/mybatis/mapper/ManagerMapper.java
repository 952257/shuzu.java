package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.Manager;
import com.springboot.mybatis.entity.ManagerCamel;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface ManagerMapper {
//    @Select("select mgr_id as id,mgr_name as name,mgr_pwd as password from t_managers where mgr_id = #{id}")
    Manager selectManagerById(Integer id);
    Set<Manager> selectAllManagers();

    @Select("    SELECT mgr_id, mgr_name, mgr_pwd\n" +
            "    FROM t_managers\n" +
            "    WHERE mgr_id = #{id}")
    ManagerCamel selectManagerCamelById(Integer id);
}