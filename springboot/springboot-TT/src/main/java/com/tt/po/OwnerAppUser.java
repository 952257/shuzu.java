package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("owner_app_user")
public class OwnerAppUser {
    @TableId(type = IdType.INPUT)
    private String appUserId;
    private String communityId;
    private String memberId;
    private String appUserName;
    private String idCard;
    private String link;
    private String roomId;
    private String roomName;
    private String state;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
