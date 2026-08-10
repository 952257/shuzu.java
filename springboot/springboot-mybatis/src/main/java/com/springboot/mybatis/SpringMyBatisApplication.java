package com.springboot.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.springboot.mybatis.mapper")
@EnableCaching
public class SpringMyBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMyBatisApplication.class, args);
    }
}
