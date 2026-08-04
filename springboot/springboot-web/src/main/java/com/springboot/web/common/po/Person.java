package com.springboot.web.common.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Long id;
    private String name;
    private double height;
    private Integer sex;
    private Date birthday;

}
