package com.springboot.mybatis;

import com.springboot.mybatis.entity.UserMyBatis;
import com.springboot.mybatis.mapper.UserMyBatisMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class UserMyBatisMapperTests {

    @Resource
    private UserMyBatisMapper userMapper;

    @Test
    public void test() throws Exception {
        userMapper.insert("AAA", 20);
        UserMyBatis u = userMapper.findByName("AAA");
        //断言
        Assertions.assertEquals(30, u.getAge().intValue());
    }

}