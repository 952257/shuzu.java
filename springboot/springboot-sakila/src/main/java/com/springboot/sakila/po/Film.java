package com.springboot.sakila.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * film表实体
 * 调整：所有id改为Long，Set类型specialFeatures改为String
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    /**
     * 电影id 主键
     */
    private Long filmId;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述 text
     */
    private String description;

    /**
     * 发布年份 year
     */
    private Integer releaseYear;

    /**
     * 语言id
     */
    private Long languageId;

    /**
     * 原始语言id
     */
    private Long originalLanguageId;

    /**
     * 租借时长
     */
    private Byte rentalDuration;

    /**
     * 租金 decimal(4,2)
     */
    private BigDecimal rentalRate;

    /**
     * 影片时长
     */
    private Short length;

    /**
     * 替换成本 decimal(5,2)
     */
    private BigDecimal replacementCost;

    /**
     * 分级 enum
     */
    private String rating;

    /**
     * 特殊标签 原MySQL set，改为字符串接收
     */
    private String specialFeatures;

    /**
     * 更新时间 timestamp
     */
    private Date lastUpdate;

}