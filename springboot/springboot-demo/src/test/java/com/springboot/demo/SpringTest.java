package com.springboot.demo;

import com.springboot.demo.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest//声明该类是一个单元测试类 JUNIT
@Slf4j
public class SpringTest {

    @Resource//将某个对象注入进来
    private Book book;

    @Resource
    private BookConfig bookConfig;

    @Resource
    private Environments enviroments;

    @Test
    public void testBook(){
        log.info("book is {}",book);
    }

    @Test
    public void testBookConfig(){
        log.info("bookConfig is {}",bookConfig);
    }

    @Test
    public void testEnviroments(){
        log.info("enviroments is {}",enviroments);
    }

    @Resource
    private RandomConfig randomConfig;

    @Test
    public void testRandomConfig(){
        log.info("randomConfig is {}",randomConfig);
    }

    @Resource
    private SpringExample springExample;

    @Test
    public void testSpringMyExample(){
        log.info("springMyExample is {}", springExample);
    }
}
