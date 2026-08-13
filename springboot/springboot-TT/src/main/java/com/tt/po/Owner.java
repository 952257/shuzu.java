package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("building_owner")
public class Owner {
    @TableId(type = IdType.INPUT)
    private String memberId;
    private String ownerId;
    private String name;
    private String sex;
    private String age;
    private String link;
    private String idCard;
    private String ownerTypeCd;
    private String personRole;
    private String communityId;
    private String address;
    private String state;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
