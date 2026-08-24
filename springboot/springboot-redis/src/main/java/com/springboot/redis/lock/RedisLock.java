package com.springboot.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
@Slf4j//分布式锁
public class RedisLock {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Value("#{T(java.util.UUID).randomUUID().toString()}")
    private String lockName;

    @Value("${redis.lock.timeout}")
    private long timeout;//获取锁的超时时间

    @Value("${redis.lock.expire}")
    private long expire;//key的失效时间
    public void lock(){
        Thread thread = Thread.currentThread();
        long start = System.currentTimeMillis();
        //如果没有获取锁，就会自旋，乐观锁
        while(true){
            //set key nx ex 30 原子性 以前要写lua保证原子性
            Boolean result = redisTemplate.opsForValue()
                    .setIfAbsent(lockName, "locked", expire, TimeUnit.MILLISECONDS);
           //成功获取锁
            if(result != null && result){
                log.debug(thread+"-成功获取锁");
                break;
            }
            log.debug(thread+"-失败获取锁");
            long current = System.currentTimeMillis();
            if(current - start > timeout){
                throw new CannotAcquireLockException("获取锁失败");
            }
        }
    }

    public void unlock(){
        Thread thread = Thread.currentThread();
        redisTemplate.delete(lockName);
        log.debug(thread+"释放锁");
    }
}
