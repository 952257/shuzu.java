package com.springboot.redis.mapper;

import com.springboot.redis.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@CacheConfig(cacheNames = "customer")
@Repository
@Slf4j
//使用Redis做缓存
public class CustomerMapper {

    @Cacheable(key = "#id")//命中缓存就不会执行该方法
    public Customer queryOne(Integer id){
        log.debug("id is {}", id);
        return new Customer(id, 1, "aaa", "bbb"
                , "aaa@bbb.com", 1, 0, new Date());

    }

    @Cacheable(key = "'some'")
    public List<Customer> querySome(){
        log.debug("-------------some-----------");
        return List.of(
                new Customer(111, 1, "aaa", "bbb", "ccc@ddd.com", 1, 1, new Date()),
                new Customer(222, 1, "bbb", "bbb", "ccc@ddd.com", 1, 1, new Date()),
                new Customer(333, 1, "ccc", "bbb", "ccc@ddd.com", 1, 1, new Date()),
                new Customer(444, 1, "ddd", "bbb", "ccc@ddd.com", 1, 1, new Date()),
                new Customer(555, 1, "eee", "bbb", "ccc@ddd.com", 1, 1, new Date())
        );
    }

    @CachePut(key = "#result.customerId")
    public Customer addOne(Customer customer){
        log.debug("addOne");
        customer.setCustomerId(777);
        return customer;
    }

    @CacheEvict(key = "#id")
    public void delOne(Integer id){
        log.debug("delOne");
    }
}