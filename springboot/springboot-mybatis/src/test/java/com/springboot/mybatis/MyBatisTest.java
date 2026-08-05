package com.springboot.mybatis;

import com.springboot.mybatis.entity.User;
import com.springboot.mybatis.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
}
