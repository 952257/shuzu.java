package com.springboot.demo;

import com.springboot.demo.aop.core.Dancer;
import com.springboot.demo.aop.core.Performer;
import com.springboot.demo.aop.core.Singer;
import com.springboot.demo.aop.syslog.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class AopTest {

    @Resource
    private Performer performer;

    @Resource
    private Singer singer;

    @Resource
    private Dancer dancer;

    @Test
    public void testAop(){
//            performer.perform();
        singer.sing();
        System.out.println("-------------------");
        dancer.dance();
    }

    @Resource
    private UserService userService;

    @Test
    public void testUserService(){
        userService.login("aaa", "123");
    }
}
