package com.springboot.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonVo {
    private Long id;
    private String name;
    private double height;
    private Integer age;
    private Date birthday;
}
