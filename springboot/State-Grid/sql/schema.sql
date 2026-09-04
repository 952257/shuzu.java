-- 国家电网数据平台：本地库结构（只含结构，不含业务数据）
CREATE DATABASE IF NOT EXISTS state_grid DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE state_grid;

CREATE TABLE IF NOT EXISTS local_user_ledger (
    id            BIGINT        NOT NULL COMMENT '远程用户ID',
    tenant_id     VARCHAR(32)   DEFAULT '000000' COMMENT '租户',
    account       VARCHAR(64)   NOT NULL COMMENT '账号',
    name          VARCHAR(64)   DEFAULT NULL COMMENT '姓名',
    real_name     VARCHAR(64)   DEFAULT NULL COMMENT '真实姓名',
    email         VARCHAR(128)  DEFAULT NULL COMMENT '邮箱',
    phone         VARCHAR(32)   DEFAULT NULL COMMENT '手机',
    role_id       VARCHAR(64)   DEFAULT NULL COMMENT '角色ID',
    dept_id       VARCHAR(64)   DEFAULT NULL COMMENT '部门ID',
    status        INT           DEFAULT 1 COMMENT '状态',
    is_deleted    INT           DEFAULT 0 COMMENT '0正常 1逻辑删除',
    create_time   DATETIME      DEFAULT NULL,
    update_time   DATETIME      DEFAULT NULL,
    sync_time     DATETIME      DEFAULT NULL COMMENT '最近同步时间',
    PRIMARY KEY (id),
    KEY idx_account (account),
    KEY idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='本地用户台账';

CREATE TABLE IF NOT EXISTS sync_report (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    report_date    DATE          NOT NULL COMMENT '统计日期',
    new_count      INT           DEFAULT 0 COMMENT '当日新增账户数',
    deleted_count  INT           DEFAULT 0 COMMENT '当日逻辑删除账户数',
    total_count    INT           DEFAULT 0 COMMENT '同步后本地总账户数',
    excel_object   VARCHAR(256)  DEFAULT NULL COMMENT 'MinIO 对象名',
    create_time    DATETIME      DEFAULT NULL,
    update_time    DATETIME      DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_report_date (report_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='台账同步日报';
