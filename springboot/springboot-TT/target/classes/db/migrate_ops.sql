USE TT;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'TT' AND TABLE_NAME = 'pay_fee_detail' AND COLUMN_NAME = 'audit_state'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE pay_fee_detail ADD COLUMN audit_state VARCHAR(12) DEFAULT ''1100'' COMMENT ''1000待审核 1100已通过 1200已拒绝''',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS tt_org (
    org_id VARCHAR(30) NOT NULL PRIMARY KEY,
    org_name VARCHAR(64) NOT NULL,
    parent_id VARCHAR(30) DEFAULT '-1',
    org_level VARCHAR(12) DEFAULT '1',
    community_id VARCHAR(30) DEFAULT NULL,
    description VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tt_notice (
    notice_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    title VARCHAR(128) NOT NULL,
    notice_type VARCHAR(12) DEFAULT '1002',
    context VARCHAR(2000) DEFAULT NULL,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '2000',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tt_vote (
    vote_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    title VARCHAR(128) NOT NULL,
    vote_type VARCHAR(12) DEFAULT '1002',
    context VARCHAR(2000) DEFAULT NULL,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '2000',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tt_visit (
    visit_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    name VARCHAR(64) NOT NULL,
    phone VARCHAR(11) DEFAULT NULL,
    car_num VARCHAR(20) DEFAULT NULL,
    visit_time DATETIME DEFAULT NULL,
    departure_time DATETIME DEFAULT NULL,
    reason VARCHAR(200) DEFAULT NULL,
    owner_name VARCHAR(64) DEFAULT NULL,
    room_name VARCHAR(64) DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1000',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tt_inspection (
    task_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    plan_name VARCHAR(64) NOT NULL,
    point_name VARCHAR(128) DEFAULT NULL,
    staff_name VARCHAR(64) DEFAULT NULL,
    inspect_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1000',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tt_purchase (
    apply_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    resource_name VARCHAR(64) NOT NULL,
    spec VARCHAR(64) DEFAULT NULL,
    quantity DECIMAL(10,2) DEFAULT 1,
    price DECIMAL(10,2) DEFAULT 0,
    apply_user VARCHAR(64) DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1000',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tt_contract (
    contract_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    contract_code VARCHAR(64) NOT NULL,
    contract_name VARCHAR(128) NOT NULL,
    contract_type VARCHAR(12) DEFAULT '1001',
    party_a VARCHAR(64) DEFAULT NULL,
    party_b VARCHAR(64) DEFAULT NULL,
    amount DECIMAL(12,2) DEFAULT 0,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '2000',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tt_discount (
    discount_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    discount_name VARCHAR(64) NOT NULL,
    discount_type VARCHAR(12) DEFAULT '1001',
    spec_value DECIMAL(10,2) DEFAULT 0,
    state VARCHAR(12) DEFAULT '1000',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT IGNORE INTO building_room (room_id, room_num, unit_id, community_id, layer, apartment, built_up_area, room_area, state, room_sub_type) VALUES
('5022081500000003', 'S101', '4022081500000001', '2022081539020475', '1', '商铺', 45.00, 40.00, '2002', '119');

INSERT IGNORE INTO tt_org (org_id, org_name, parent_id, org_level, community_id, description) VALUES
('ORG0000000000001', 'HC物业公司', '-1', '1', '2022081539020475', '总部'),
('ORG0000000000002', '客服中心', 'ORG0000000000001', '2', '2022081539020475', '业主服务'),
('ORG0000000000003', '工程部', 'ORG0000000000001', '2', '2022081539020475', '维修巡检');

INSERT IGNORE INTO tt_notice (notice_id, community_id, title, notice_type, context, start_time, end_time, state) VALUES
('N022081500000001', '2022081539020475', '停水通知', '1001', '明日 9:00-12:00 市政检修停水，请提前储水。', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), '2000'),
('N022081500000002', '2022081539020475', '小区环境整治公告', '1002', '本周六开展公共区域清洁，请将楼道杂物自行收回。', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), '2000');

INSERT IGNORE INTO tt_vote (vote_id, community_id, title, vote_type, context, start_time, end_time, state) VALUES
('V022081500000001', '2022081539020475', '是否增设充电车位', '1002', '拟在地下车库增设 8 个充电车位，请业主投票。', NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), '2000');

INSERT IGNORE INTO tt_visit (visit_id, community_id, name, phone, car_num, visit_time, reason, owner_name, room_name, state) VALUES
('I022081500000001', '2022081539020475', '王强', '13900002222', '青A88888', NOW(), '探访亲友', '李明', '1-1-101', '2000');

INSERT IGNORE INTO tt_inspection (task_id, community_id, plan_name, point_name, staff_name, inspect_time, state, remark) VALUES
('P022081500000001', '2022081539020475', '日常安防巡检', '1号楼大堂', '吴学文', NOW(), '2000', '设备正常'),
('P022081500000002', '2022081539020475', '消防巡检', '地下车库消火栓', NULL, DATE_ADD(NOW(), INTERVAL 1 DAY), '1000', NULL);

INSERT IGNORE INTO tt_purchase (apply_id, community_id, resource_name, spec, quantity, price, apply_user, state, remark) VALUES
('U022081500000001', '2022081539020475', 'LED节能灯', '18W', 50, 12.50, '吴学文', '1000', '楼道更换');

INSERT IGNORE INTO tt_contract (contract_id, community_id, contract_code, contract_name, contract_type, party_a, party_b, amount, start_time, end_time, state) VALUES
('T022081500000001', '2022081539020475', 'WY-2026-001', '物业服务合同', '1001', '业主委员会', 'HC物业公司', 120000.00, NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), '2000');

INSERT IGNORE INTO tt_discount (discount_id, community_id, discount_name, discount_type, spec_value, state, remark) VALUES
('D022081500000001', '2022081539020475', '预缴一年九折', '1001', 0.90, '1000', '一次性预缴12个月'),
('D022081500000002', '2022081539020475', '困难户减免', '2002', 50.00, '1000', '每月减免50元');
