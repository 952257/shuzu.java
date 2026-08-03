package com.springboot.demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "environments")
@Data
public class Enviroments {

    private Dev dev;

    private Prod prod;

    //可使用静态内部类
    @Data
   public static class Dev{
        private String url;

        private String name;
    }

    @Data
    public static class Prod{
        private String url;

        private String name;
    }

}