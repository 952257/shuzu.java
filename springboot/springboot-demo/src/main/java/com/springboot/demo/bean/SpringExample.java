package com.springboot.demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.my-example")
@Data
public class SpringExample {

    private List<String> url;

    private Map<String,String> auth;
}
