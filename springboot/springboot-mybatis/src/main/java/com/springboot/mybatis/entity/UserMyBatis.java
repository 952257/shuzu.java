package com.springboot.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMyBatis {

    private Long id;
    private String name;
    private Integer age;

    public UserMyBatis(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}