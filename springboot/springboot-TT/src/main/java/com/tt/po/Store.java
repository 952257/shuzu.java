package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("s_store")
public class Store {
    @TableId(type = IdType.INPUT)
    private String storeId;
    private String name;
    private String tel;
    private String address;
    private String nearbyLandmarks;
    private String corporation;
    private String foundingTime;
    private String state;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
