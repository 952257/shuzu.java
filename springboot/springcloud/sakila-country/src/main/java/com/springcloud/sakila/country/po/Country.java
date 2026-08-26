package com.springcloud.sakila.country.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("country")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    @TableId(type = IdType.AUTO)
    private Integer countryId;
    private String country;
    private Date lastUpdate;
}
