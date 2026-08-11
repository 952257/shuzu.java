package com.springboot.demo.ioc;

import org.springframework.stereotype.Component;

@Component
public class C implements BIn{


    @Override
    public void bbb() {
        System.out.println("ccc");
    }
}
