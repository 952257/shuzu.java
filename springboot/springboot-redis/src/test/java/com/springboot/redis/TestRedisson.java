package com.springboot.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class TestRedisson {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public  void testLock() throws InterruptedException {
        Thread th1= new Thread(this::task);
        Thread th2 = new Thread(this::task);
        th1.start();
        th2.start();
        th1.join();
        th2.join();
    }

    private void task(){
        //分布式锁
        RLock lock = redissonClient.getLock("redissonLock");
        lock.lock();
        try {
            System.out.println(Thread.currentThread() + "开始执行");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread() + "执行结束");
        } finally {
            lock.unlock();
        }
    }


    @Test
    public void testB(){
        RBloomFilter<Object> mybloom = redissonClient.getBloomFilter("mybloom");
        mybloom.tryInit(1000, 0.01);
        for (int i = 0; i < 1000; i++) {
            mybloom.add("username-"+i);
        }
        System.out.println(mybloom.getHashIterations());
        int count = 0;
        for (int i = 1000; i < 2000; i++) {
            boolean contains = mybloom.contains("username-" + i);
            if(contains) count++;
        }
        System.out.println(count);
    }
}