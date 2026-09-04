package com.zhrj.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("sync_report")
public class SyncReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDate reportDate;
    private Integer newCount;
    private Integer deletedCount;
    private Integer totalCount;
    private String excelObject;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
