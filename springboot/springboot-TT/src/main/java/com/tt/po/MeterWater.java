package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("meter_water")
public class MeterWater {
    @TableId(type = IdType.INPUT)
    private String waterId;
    private String communityId;
    private String objId;
    private String objType;
    private String meterType;
    private BigDecimal preDegrees;
    private BigDecimal curDegrees;
    private Date preReadingTime;
    private Date curReadingTime;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
