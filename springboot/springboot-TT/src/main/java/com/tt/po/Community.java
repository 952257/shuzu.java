package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("community")
public class Community {
    @TableId(type = IdType.INPUT)
    private String communityId;
    private String name;
    private String address;
    private String cityCode;
    private String cityName;
    private String mapX;
    private String mapY;
    private String nearbyLandmarks;
    private String tel;
    private Integer payFeeMonth;
    private Integer feePrice;
    private String state;
    private String storeId;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
