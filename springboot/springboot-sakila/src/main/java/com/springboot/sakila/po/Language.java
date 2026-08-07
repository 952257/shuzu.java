package com.springboot.sakila.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    private Integer id;

    private String name;

    private Date lastUpdate;
}
