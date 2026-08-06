package com.springboot.mybatis.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Passport {
    private Integer id;
    private String nationality;
    private Date expire;
    private Integer passengerId;

    //关联关系 按照关系的强弱 关联->合成 聚合
    private Passenger passenger;
}