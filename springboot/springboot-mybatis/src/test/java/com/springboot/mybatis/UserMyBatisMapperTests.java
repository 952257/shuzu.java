package com.springboot.mybatis;

import com.springboot.mybatis.entity.UserMyBatis;
import com.springboot.mybatis.mapper.UserMyBatisMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
@Transactional
public class UserMyBatisMapperTests {

    @Resource
    private UserMyBatisMapper userMyBatisMapper;

    @Test
    public void test() throws Exception {
        // 使用唯一名称，避免库中已有同名数据导致 selectOne 查出多行
        String name = "AAA-" + System.currentTimeMillis();
        userMyBatisMapper.insert(name, 20);
        UserMyBatis u = userMyBatisMapper.findByName(name);
        Assertions.assertEquals(20, u.getAge().intValue());
    }

}