package com.stategrid;

import com.stategrid.config.MinioProperties;
import com.stategrid.config.RemoteAuthProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.stategrid.mapper")
@EnableConfigurationProperties({RemoteAuthProperties.class, MinioProperties.class})
public class StateGridApplication {

    public static void main(String[] args) {
        SpringApplication.run(StateGridApplication.class, args);
    }
}
