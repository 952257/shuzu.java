package com.springboot.mybatis;

import com.springboot.mybatis.entity.UserMyBatis;
import com.springboot.mybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class CacheTest {

    @Resource
    private CacheManager cacheManager;

    @Resource
    private UserService userService;

    @Test
    public void testCache(){
        userService.addOne(new UserMyBatis("QQQ", 20));
        log.info("success");
    }

    @Test
    public void testCache2(){
        UserMyBatis userMyBatis = userService.queryById(3L);
        UserMyBatis userMyBatis2 = userService.queryById(3L);
        log.info("userMybatis is {}",userMyBatis);
    }
}
