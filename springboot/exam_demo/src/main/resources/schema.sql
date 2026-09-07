CREATE TABLE IF NOT EXISTS local_user_ledger (
    id              BIGINT       NOT NULL COMMENT '远程用户主键',
    tenant_id       VARCHAR(12)  NULL COMMENT '租户',
    account         VARCHAR(45)  NULL COMMENT '账号',
    name            VARCHAR(20)  NULL COMMENT '昵称',
    real_name       VARCHAR(10)  NULL COMMENT '姓名',
    email           VARCHAR(45)  NULL COMMENT '邮箱',
    phone           VARCHAR(45)  NULL COMMENT '手机',
    sex             INT          NULL COMMENT '性别',
    status          INT          NULL COMMENT '状态',
    is_deleted      INT          NOT NULL DEFAULT 0 COMMENT '是否逻辑删除',
    last_sync_time  DATETIME     NULL COMMENT '最近同步时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='本地用户信息台账';

CREATE TABLE IF NOT EXISTS sync_report (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    report_date   DATE         NOT NULL COMMENT '统计日期',
    new_count     INT          NOT NULL DEFAULT 0 COMMENT '新增账户数',
    deleted_count INT          NOT NULL DEFAULT 0 COMMENT '已删除账户数',
    total_count   INT          NOT NULL DEFAULT 0 COMMENT '台账总条数',
    excel_object  VARCHAR(255) NULL COMMENT 'MinIO 对象名',
    created_at    DATETIME     NULL COMMENT '生成时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_report_date (report_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账同步日报';
