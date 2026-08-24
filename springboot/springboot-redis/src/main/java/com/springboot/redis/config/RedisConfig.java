package com.springboot.redis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

	/**
	 * 注册RedisTemplate
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory
			){
		RedisTemplate<String,Object> redis = new RedisTemplate<>();
		redis.setConnectionFactory(redisConnectionFactory);
		redis.setKeySerializer(RedisSerializer.string());
		//值使用JSON序列化器
		redis.setValueSerializer(RedisSerializer.json());
		//Hash类型值序列化
		redis.setHashKeySerializer(RedisSerializer.string());
		redis.setHashValueSerializer(RedisSerializer.json());
		redis.afterPropertiesSet();
		return redis;
	}


	/**
	 * 配置redis缓存管理器
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration= RedisCacheConfiguration.defaultCacheConfig().
				//设置缓存1天后失效
				disableCachingNullValues().entryTtl(Duration.ofSeconds(60*60*24)).
				serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())).
				serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
		RedisCacheWriter cacheWriter= RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        return new RedisCacheManager(cacheWriter,redisCacheConfiguration);
	}
}