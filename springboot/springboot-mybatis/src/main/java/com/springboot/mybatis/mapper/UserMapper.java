package com.springboot.mybatis.mapper;

import com.springboot.mybatis.entity.User;
import org.apache.ibatis.annotations.*;

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

    @Update("UPDATE t_users SET name=#{name}, password=#{password}, sex=#{sex}, birthday=#{birthday} " +
            "    WHERE id = #{id}")
    void updateUser(User user);

    @Insert("INSERT INTO t_users\n" +
            "       VALUES (#{id}, #{name}, #{password}, #{sex}, #{birthday}, #{registTime});")
    //逐渐回填
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertUser(User user);

    @Delete(" DELETE FROM t_users WHERE id = #{id} ")
    void deleteUser(Integer id);
}
