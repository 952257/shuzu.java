package com.springboot.demo.config;

import com.springboot.demo.ioc.C;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public C c(){
        return new C();
    }
}
