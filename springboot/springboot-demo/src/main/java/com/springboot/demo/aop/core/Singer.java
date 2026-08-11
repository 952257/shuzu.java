package com.springboot.demo.aop.core;

import org.springframework.stereotype.Component;

@Component
public class Singer {
    public void sing(){
        System.out.println("歌手在唱歌");
    }
}
