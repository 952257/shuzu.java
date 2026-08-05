package com.springboot.mybatis;

import com.springboot.mybatis.entity.User;
import com.springboot.mybatis.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class MyBatisTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectUserById(){
        User user = userMapper.selectUserById(1);
        log.info("user is {}", user);
    }

    @Test
    public void testSelectUserByIdAndPwd(){
        User user = userMapper.selectUserByIdAndPwd(1, "123");
        log.info("user is {}", user);
    }


    @Test
    public void testSelectUserByIdAndPwd3(){
        Map<String, Object> values = new HashMap<>();
        values.put("id", 1);
        values.put("pwd", "123");
        User user = userMapper.selectUserByIdAndPwd3(values);
        log.info("user is {}", user);
    }

    @Test
    public void testSelectUserByUserInfo(){
        User condition = new User();
        condition.setId(1);
        condition.setPassword("123");
        User user = userMapper.selectUserByUserInfo(condition);
        log.info("user is {}", user);
    }

    @Test
    public void testSelectUsersByKeyword(){
        List<User> users = userMapper.selectUsersByKeyword("c");
        log.info("users is {}", users);
    }


    @Test
    public void testUpdateUser(){
        User user = new User(4, "张三", "456",
                "男", null, null);
        userMapper.updateUser(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User(null, "sadfqaf", "456",
                "男", null, new Date());
        userMapper.insertUser(user);
        log.info("user is {}", user);
    }

    @Test
    public void testDeleteUser(){

        userMapper.deleteUser(4);
    }
}
