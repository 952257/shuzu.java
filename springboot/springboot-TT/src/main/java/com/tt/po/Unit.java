package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("building_unit")
public class Unit {
    @TableId(type = IdType.INPUT)
    private String unitId;
    private String unitNum;
    private String floorId;
    private Integer layerCount;
    private String lift;
    private BigDecimal unitArea;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
