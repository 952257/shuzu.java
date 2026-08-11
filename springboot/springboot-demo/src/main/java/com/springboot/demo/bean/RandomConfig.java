package com.springboot.demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rand")
@Data
public class RandomConfig {

    private String aaa;

    private int bbb;

    private int ccc;

    private int ddd;
}