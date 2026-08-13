package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("parking_space")
public class ParkingSpace {
    @TableId(type = IdType.INPUT)
    private String psId;
    private String num;
    private String communityId;
    private String paId;
    private String parkingType;
    private String state;
    private BigDecimal area;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
