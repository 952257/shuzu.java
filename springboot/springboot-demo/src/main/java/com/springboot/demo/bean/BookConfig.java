package com.springboot.demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration//该类是一个配置对象
@ConfigurationProperties(prefix = "book")
@Data
public class BookConfig {

    private String name;

    private String author;
}