package com.springboot.web.config;

import com.springboot.web.common.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfiguration implements WebMvcConfigurer {

    @Bean
    public MyInterceptor firstInterceptor() {
        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器 拦截所有url
        registry.addInterceptor(this.firstInterceptor()).addPathPatterns("/**");

    }
    
}