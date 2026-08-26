package com.springcloud.sakila.city.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private Integer countryId;
    private String country;
    private Date lastUpdate;
}
