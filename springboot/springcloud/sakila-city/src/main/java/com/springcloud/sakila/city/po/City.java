package com.springcloud.sakila.city.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("city")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @TableId(type = IdType.AUTO)
    private Integer cityId;
    private String city;
    private Integer countryId;
    private Date lastUpdate;
}
