package com.springboot.demo.aop.core;

import org.springframework.stereotype.Component;

/**
 * 演员
 */
@Component
public class Performer {

    public void perform(){
        System.out.println("演员表演");
        if(Math.random() > 0.5)
            throw  new RuntimeException("演砸了");
    }
}
