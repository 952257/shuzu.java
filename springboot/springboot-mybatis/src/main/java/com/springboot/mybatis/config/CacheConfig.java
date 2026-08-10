package com.springboot.mybatis.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 配置Caffeine缓存属性
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                // 过期时间：写入后30分钟过期
                .expireAfterWrite(30, TimeUnit.MINUTES)
                // 最大缓存数量：1000条
                .maximumSize(1000);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}