package com.springboot.demo.ioc;

import org.springframework.stereotype.Component;

@Component
public class B implements BIn{

    public void bbb(){
        System.out.println("bbb");
    }
}
