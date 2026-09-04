CREATE TABLE IF NOT EXISTS local_user_ledger (
    id            BIGINT        NOT NULL,
    tenant_id     VARCHAR(32)   DEFAULT '000000',
    account       VARCHAR(64)   NOT NULL,
    name          VARCHAR(64),
    real_name     VARCHAR(64),
    email         VARCHAR(128),
    phone         VARCHAR(32),
    role_id       VARCHAR(64),
    dept_id       VARCHAR(64),
    status        INT           DEFAULT 1,
    is_deleted    INT           DEFAULT 0,
    create_time   TIMESTAMP,
    update_time   TIMESTAMP,
    sync_time     TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sync_report (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_date    DATE          NOT NULL,
    new_count      INT           DEFAULT 0,
    deleted_count  INT           DEFAULT 0,
    total_count    INT           DEFAULT 0,
    excel_object   VARCHAR(512),
    create_time    TIMESTAMP,
    update_time    TIMESTAMP,
    UNIQUE (report_date)
);
