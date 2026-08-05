package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    User selectUserById(Integer id);

    User selectUserByIdAndPwd(@Param("id")Integer id , @Param("pwd")String pwd);

    // 添加 Map 进行参数绑定；列名是 password，#{pwd} 对应 map 的 key
    @Select("SELECT * FROM t_users WHERE id = #{id} AND password = #{pwd}")
    User selectUserByIdAndPwd3(Map values);

    //使用对象属性进行参数绑定
    @Select("SELECT * FROM t_users WHERE ID = #{id} AND password = #{password}")
    User selectUserByUserInfo(User user);

    List<User> selectUsersByKeyword(String keyword);
}
