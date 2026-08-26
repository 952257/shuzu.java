package com.springcloud.openfeign;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OpenFeignConfig {

    @Bean
     public Logger.Level feignLoggerLevel(){
         return Logger.Level.FULL;
     }


    /**
     * 超时时间配置
     * @return
     */
    @Bean
    public Request.Options options(){
        return new Request.Options(5000, TimeUnit.MILLISECONDS,
                5000,TimeUnit.MILLISECONDS,true);
    }

    /**
     * 超时重试
     *
     * period：周期，重试间隔时间
     * maxPeriod：最大周期，重试间隔时间按照一定的规则逐渐增大，但不能超过最大周期
     * maxAttempts：最大尝试次数，重试次数
     * @return
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 5); // 这里可以自定义重试间隔和重试时间上限
    }
}