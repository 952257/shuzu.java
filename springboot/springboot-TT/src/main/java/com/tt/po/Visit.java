package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tt_visit")
public class Visit {
    @TableId(type = IdType.INPUT)
    private String visitId;
    private String communityId;
    private String name;
    private String phone;
    private String carNum;
    private Date visitTime;
    private Date departureTime;
    private String reason;
    private String ownerName;
    private String roomName;
    private String state;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
