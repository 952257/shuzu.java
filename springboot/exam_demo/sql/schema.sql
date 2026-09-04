-- 在官方 exam_demo 的 MySQL 库 ry-vue 中执行（application-druid.yml）
CREATE DATABASE IF NOT EXISTS `ry-vue` DEFAULT CHARACTER SET utf8mb4;
USE `ry-vue`;

CREATE TABLE IF NOT EXISTS local_user_ledger (
    id            BIGINT        NOT NULL COMMENT '远程用户ID',
    tenant_id     VARCHAR(32)   DEFAULT '000000',
    account       VARCHAR(64)   NOT NULL,
    name          VARCHAR(64),
    real_name     VARCHAR(64),
    email         VARCHAR(128),
    phone         VARCHAR(32),
    role_id       VARCHAR(64),
    dept_id       VARCHAR(64),
    status        INT           DEFAULT 1,
    is_deleted    INT           DEFAULT 0 COMMENT '0正常 1逻辑删除',
    create_time   DATETIME,
    update_time   DATETIME,
    sync_time     DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sync_report (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_date    DATE          NOT NULL,
    new_count      INT           DEFAULT 0,
    deleted_count  INT           DEFAULT 0,
    total_count    INT           DEFAULT 0,
    excel_object   VARCHAR(512),
    create_time    DATETIME,
    update_time    DATETIME,
    UNIQUE KEY uk_report_date (report_date)
);
