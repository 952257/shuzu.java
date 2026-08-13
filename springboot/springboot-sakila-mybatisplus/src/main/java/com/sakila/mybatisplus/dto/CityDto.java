package com.sakila.mybatisplus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {

    private Integer cityId;

    private String city;

    private Integer countryId;

    private String country;

}
