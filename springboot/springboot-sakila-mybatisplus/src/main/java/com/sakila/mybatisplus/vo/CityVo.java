package com.sakila.mybatisplus.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityVo {

    private Integer cityId;

    private String city;

    private Integer countryId;

    private Date lastUpdate;

    private String country;
}
