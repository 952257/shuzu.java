package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("owner_car")
public class OwnerCar {
    @TableId(type = IdType.INPUT)
    private String carId;
    private String ownerId;
    private String communityId;
    private String carNum;
    private String carBrand;
    private String carType;
    private String carColor;
    private String psId;
    private Date startTime;
    private Date endTime;
    private String state;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
