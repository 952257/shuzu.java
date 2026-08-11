package com.springboot.demo;

import com.springboot.demo.ioc.A;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class IocTest {

    @Autowired
    private A a;

    @Test
    public void testA(){
        a.aaa();
    }
}
