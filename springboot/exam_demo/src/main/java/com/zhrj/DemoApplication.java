package com.zhrj;

import com.zhrj.exam.config.RemoteAuthProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhrj
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.zhrj.exam.mapper")
@EnableConfigurationProperties(RemoteAuthProperties.class)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
