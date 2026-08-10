package com.springboot.mybatis;

import com.springboot.mybatis.entity.UserMyBatis;
import com.springboot.mybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class TransactionTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddTwoUser(){
        userService.addTwo(
                new UserMyBatis("BBB", 20),
                new UserMyBatis("BBB", 30));
    }
}
