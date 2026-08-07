package com.springboot.sakila.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * film表实体
 * 调整：所有id改为Long，Set类型specialFeatures改为String
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmVo {

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

    //语言名称
    private String languageName;

    //多个演员姓名
    private String actorNames;

}