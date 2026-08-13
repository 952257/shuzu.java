package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("r_repair_pool")
public class Repair {
    @TableId(type = IdType.INPUT)
    private String repairId;
    private String communityId;
    private String repairName;
    private String repairType;
    private Date appointmentTime;
    private String tel;
    private String roomId;
    private String context;
    private String state;
    private String repairObjName;
    private String staffId;
    private String staffName;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
