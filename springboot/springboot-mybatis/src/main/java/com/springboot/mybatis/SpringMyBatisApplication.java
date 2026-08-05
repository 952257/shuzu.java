package com.springboot.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.springboot.mybatis.mapper")
public class SpringMyBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMyBatisApplication.class, args);
    }
}
