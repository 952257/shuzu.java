package com.springboot.redis;

import com.springboot.redis.entity.Customer;
import com.springboot.redis.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class TestRedisCache {

    @Autowired
    private CustomerMapper customerMapper;
    @Test
    public void testCache(){
//       customerMapper.querySome();
        Customer customer = new Customer(null, 1,
                "ccc", "bbb", "qqq@www.com",
                1, 1, new Date());
//        customerMapper.addOne(customer);
        customerMapper.delOne(777);
    }
    
}