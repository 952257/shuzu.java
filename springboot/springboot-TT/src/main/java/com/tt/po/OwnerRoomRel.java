package com.tt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("building_owner_room_rel")
public class OwnerRoomRel {
    @TableId(type = IdType.INPUT)
    private String relId;
    private String ownerId;
    private String roomId;
    private String state;
    private Date startTime;
    private Date endTime;
    private String remark;
    @TableLogic
    private String statusCd;
    private Date createTime;
}
