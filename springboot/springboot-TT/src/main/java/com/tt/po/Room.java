package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("building_room")
public class Room {
    @TableId(type = IdType.INPUT)
    private String roomId;
    private String roomNum;
    private String unitId;
    private String communityId;
    private String layer;
    private String apartment;
    private BigDecimal builtUpArea;
    private BigDecimal roomArea;
    private BigDecimal roomRent;
    private String state;
    private String roomSubType;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
