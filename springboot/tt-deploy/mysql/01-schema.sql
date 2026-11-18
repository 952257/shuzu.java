CREATE DATABASE IF NOT EXISTS TT DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE TT;

DROP TABLE IF EXISTS account_detail;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS owner_app_user;
DROP TABLE IF EXISTS pay_fee_detail;
DROP TABLE IF EXISTS pay_fee;
DROP TABLE IF EXISTS pay_fee_config;
DROP TABLE IF EXISTS tt_community_setting;
DROP TABLE IF EXISTS tt_discount;
DROP TABLE IF EXISTS meter_water;
DROP TABLE IF EXISTS r_repair_pool;
DROP TABLE IF EXISTS complaint;
DROP TABLE IF EXISTS tt_inspection;
DROP TABLE IF EXISTS tt_purchase;
DROP TABLE IF EXISTS tt_contract;
DROP TABLE IF EXISTS tt_visit;
DROP TABLE IF EXISTS tt_notice;
DROP TABLE IF EXISTS tt_vote;
DROP TABLE IF EXISTS tt_org;
DROP TABLE IF EXISTS owner_car;
DROP TABLE IF EXISTS parking_space;
DROP TABLE IF EXISTS building_owner_room_rel;
DROP TABLE IF EXISTS building_owner;
DROP TABLE IF EXISTS building_room;
DROP TABLE IF EXISTS building_unit;
DROP TABLE IF EXISTS f_floor;
DROP TABLE IF EXISTS community;
DROP TABLE IF EXISTS user_login;
DROP TABLE IF EXISTS store_user;
DROP TABLE IF EXISTS s_store;
DROP TABLE IF EXISTS u_user;

CREATE TABLE u_user (
    user_id VARCHAR(30) NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    username VARCHAR(64) NOT NULL,
    tel VARCHAR(11) DEFAULT NULL,
    password VARCHAR(64) NOT NULL,
    role VARCHAR(12) NOT NULL COMMENT 'ADMIN / STAFF',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '0在用 1失效',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE s_store (
    store_id VARCHAR(30) NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    tel VARCHAR(20) DEFAULT NULL,
    address VARCHAR(255) DEFAULT NULL,
    nearby_landmarks VARCHAR(128) DEFAULT NULL,
    corporation VARCHAR(64) DEFAULT NULL,
    founding_time VARCHAR(20) DEFAULT NULL,
    state VARCHAR(12) NOT NULL DEFAULT '48001' COMMENT '48001正常 48002禁用',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE store_user (
    store_user_id VARCHAR(30) NOT NULL PRIMARY KEY,
    store_id VARCHAR(30) NOT NULL,
    user_id VARCHAR(30) NOT NULL,
    rel_cd VARCHAR(12) NOT NULL DEFAULT '600311000001' COMMENT '管理员/员工关系',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_login (
    login_id VARCHAR(30) NOT NULL PRIMARY KEY,
    user_id VARCHAR(30) NOT NULL,
    user_name VARCHAR(64) DEFAULT NULL,
    login_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(12) DEFAULT 'WEB'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE community (
    community_id VARCHAR(30) NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    address VARCHAR(255) DEFAULT NULL,
    city_code VARCHAR(12) DEFAULT NULL,
    city_name VARCHAR(128) DEFAULT NULL,
    map_x VARCHAR(20) DEFAULT NULL,
    map_y VARCHAR(20) DEFAULT NULL,
    nearby_landmarks VARCHAR(128) DEFAULT NULL,
    tel VARCHAR(20) DEFAULT NULL,
    pay_fee_month INT DEFAULT 12,
    fee_price INT DEFAULT 0,
    state VARCHAR(12) NOT NULL DEFAULT '1100' COMMENT '1100审核完成',
    store_id VARCHAR(30) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE f_floor (
    floor_id VARCHAR(30) NOT NULL PRIMARY KEY,
    floor_num VARCHAR(12) NOT NULL,
    name VARCHAR(64) NOT NULL,
    community_id VARCHAR(30) NOT NULL,
    floor_area DECIMAL(12,2) DEFAULT 0,
    seq INT DEFAULT 1,
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE building_unit (
    unit_id VARCHAR(30) NOT NULL PRIMARY KEY,
    unit_num VARCHAR(12) NOT NULL,
    floor_id VARCHAR(30) NOT NULL,
    layer_count INT NOT NULL DEFAULT 1,
    lift VARCHAR(4) NOT NULL DEFAULT '2020' COMMENT '1010有电梯 2020无',
    unit_area DECIMAL(10,2) DEFAULT 0,
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE building_room (
    room_id VARCHAR(30) NOT NULL PRIMARY KEY,
    room_num VARCHAR(64) NOT NULL,
    unit_id VARCHAR(30) NOT NULL,
    community_id VARCHAR(30) NOT NULL,
    layer VARCHAR(12) DEFAULT '1',
    apartment VARCHAR(20) DEFAULT '两室一厅',
    built_up_area DECIMAL(12,2) DEFAULT 0,
    room_area DECIMAL(12,2) DEFAULT 0,
    room_rent DECIMAL(10,2) DEFAULT 0,
    state VARCHAR(4) NOT NULL DEFAULT '2001' COMMENT '2001未售 2002已售',
    room_sub_type VARCHAR(12) DEFAULT '110' COMMENT '110住宅 119办公室',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE building_owner (
    member_id VARCHAR(30) NOT NULL PRIMARY KEY,
    owner_id VARCHAR(30) NOT NULL,
    name VARCHAR(64) NOT NULL,
    sex VARCHAR(12) DEFAULT '0',
    age VARCHAR(12) DEFAULT NULL,
    link VARCHAR(11) NOT NULL,
    id_card VARCHAR(64) DEFAULT NULL,
    owner_type_cd VARCHAR(4) NOT NULL DEFAULT '1001' COMMENT '1001业主 1002家庭成员',
    person_role VARCHAR(12) DEFAULT '1' COMMENT '1业主 2租客 3家庭成员',
    community_id VARCHAR(30) DEFAULT NULL,
    address VARCHAR(255) DEFAULT NULL,
    state VARCHAR(12) NOT NULL DEFAULT '2000' COMMENT '1000待审核 2000审核完成 3000拒绝',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE building_owner_room_rel (
    rel_id VARCHAR(30) NOT NULL PRIMARY KEY,
    owner_id VARCHAR(30) NOT NULL,
    room_id VARCHAR(30) NOT NULL,
    state VARCHAR(4) NOT NULL DEFAULT '2002' COMMENT '2001未迁入 2002迁入 2003迁出',
    start_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    end_time DATETIME DEFAULT NULL,
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE parking_space (
    ps_id VARCHAR(30) NOT NULL PRIMARY KEY,
    num VARCHAR(12) NOT NULL,
    community_id VARCHAR(30) NOT NULL,
    pa_id VARCHAR(30) DEFAULT NULL,
    parking_type VARCHAR(12) DEFAULT '1' COMMENT '1地上 2地下',
    state VARCHAR(4) NOT NULL DEFAULT 'F' COMMENT 'F空闲 S已售 H已出租',
    area DECIMAL(10,2) DEFAULT 0,
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE owner_car (
    car_id VARCHAR(30) NOT NULL PRIMARY KEY,
    owner_id VARCHAR(30) NOT NULL,
    community_id VARCHAR(30) NOT NULL,
    car_num VARCHAR(20) NOT NULL,
    car_brand VARCHAR(64) DEFAULT NULL,
    car_type VARCHAR(12) DEFAULT '9901',
    car_color VARCHAR(12) DEFAULT NULL,
    ps_id VARCHAR(30) DEFAULT NULL,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1001',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pay_fee_config (
    config_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    fee_type_cd VARCHAR(12) NOT NULL COMMENT '888800010001物业费 888800010002水费 888800010003电费',
    fee_name VARCHAR(64) NOT NULL,
    fee_flag VARCHAR(12) DEFAULT '1003006' COMMENT '1003006周期性 2006012一次性',
    computing_formula VARCHAR(12) DEFAULT '1001',
    square_price DECIMAL(10,2) DEFAULT 0,
    additional_amount DECIMAL(10,2) DEFAULT 0,
    bill_type VARCHAR(12) DEFAULT '001',
    payment_cycle VARCHAR(12) DEFAULT '12',
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pay_fee (
    fee_id VARCHAR(30) NOT NULL PRIMARY KEY,
    config_id VARCHAR(30) NOT NULL,
    community_id VARCHAR(30) NOT NULL,
    payer_obj_id VARCHAR(30) NOT NULL COMMENT '房屋或车位ID',
    payer_obj_type VARCHAR(12) DEFAULT '3333' COMMENT '3333房屋 6666车位',
    fee_name VARCHAR(64) DEFAULT NULL,
    amount DECIMAL(10,2) DEFAULT 0,
    state VARCHAR(12) NOT NULL DEFAULT '2008001' COMMENT '2008001收费中 2009001收费结束',
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pay_fee_detail (
    detail_id VARCHAR(30) NOT NULL PRIMARY KEY,
    fee_id VARCHAR(30) NOT NULL,
    community_id VARCHAR(30) NOT NULL,
    cycles DECIMAL(10,2) DEFAULT 1,
    receivable_amount DECIMAL(10,2) DEFAULT 0,
    received_amount DECIMAL(10,2) DEFAULT 0,
    pay_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1400' COMMENT '1400正常 1500退费',
    audit_state VARCHAR(12) DEFAULT '1100' COMMENT '1000待审核 1100已通过 1200已拒绝',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE meter_water (
    water_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    obj_id VARCHAR(30) NOT NULL,
    obj_type VARCHAR(12) DEFAULT '3333',
    meter_type VARCHAR(12) NOT NULL COMMENT '2020水表 3030电表',
    pre_degrees DECIMAL(10,2) DEFAULT 0,
    cur_degrees DECIMAL(10,2) DEFAULT 0,
    pre_reading_time DATETIME DEFAULT NULL,
    cur_reading_time DATETIME DEFAULT NULL,
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE r_repair_pool (
    repair_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    repair_name VARCHAR(64) NOT NULL,
    repair_type VARCHAR(12) DEFAULT '1001',
    appointment_time DATETIME DEFAULT NULL,
    tel VARCHAR(11) DEFAULT NULL,
    room_id VARCHAR(30) DEFAULT NULL,
    context VARCHAR(500) DEFAULT NULL,
    state VARCHAR(12) NOT NULL DEFAULT '1000' COMMENT '1000待处理 1100处理中 1200已完成 1300已评价',
    repair_obj_name VARCHAR(128) DEFAULT NULL,
    staff_id VARCHAR(30) DEFAULT NULL,
    staff_name VARCHAR(64) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE complaint (
    complaint_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    type_cd VARCHAR(12) DEFAULT '809001' COMMENT '809001投诉 809002建议',
    complaint_name VARCHAR(64) DEFAULT NULL,
    tel VARCHAR(11) DEFAULT NULL,
    room_id VARCHAR(30) DEFAULT NULL,
    context VARCHAR(500) DEFAULT NULL,
    state VARCHAR(12) NOT NULL DEFAULT '10001' COMMENT '10001待处理 10002处理中 10003已完成',
    current_user_id VARCHAR(30) DEFAULT NULL,
    current_user_name VARCHAR(64) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE account (
    acct_id VARCHAR(30) NOT NULL PRIMARY KEY,
    acct_name VARCHAR(64) DEFAULT NULL,
    obj_id VARCHAR(30) NOT NULL COMMENT '业主ID',
    obj_type VARCHAR(12) DEFAULT '2002',
    community_id VARCHAR(30) NOT NULL,
    amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    acct_type VARCHAR(12) DEFAULT '2003' COMMENT '2003现金账户',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE account_detail (
    detail_id VARCHAR(30) NOT NULL PRIMARY KEY,
    acct_id VARCHAR(30) NOT NULL,
    detail_type VARCHAR(12) NOT NULL COMMENT '1001转入 2002转出 3003撤销',
    amount DECIMAL(12,2) NOT NULL,
    remark VARCHAR(200) DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1001' COMMENT '1001正常 2002已撤销',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE owner_app_user (
    app_user_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    member_id VARCHAR(30) DEFAULT NULL,
    app_user_name VARCHAR(64) DEFAULT NULL,
    id_card VARCHAR(64) DEFAULT NULL,
    link VARCHAR(11) DEFAULT NULL,
    room_id VARCHAR(30) DEFAULT NULL,
    room_name VARCHAR(128) DEFAULT NULL,
    state VARCHAR(12) NOT NULL DEFAULT '10000' COMMENT '10000待审核 12000审核成功 13000审核失败',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始数据：密码均为 admin，加密规则 md5(md5(passwd + 'hc@java110'))
INSERT INTO u_user (user_id, name, username, tel, password, role) VALUES
('1000000001', '系统管理员', 'admin', '18909711443', '3ad352384261cbc2c7462210dbb3ce61', 'ADMIN'),
('1000000002', '吴学文', 'wuxw', '18909711555', '3ad352384261cbc2c7462210dbb3ce61', 'STAFF');

INSERT INTO s_store (store_id, name, tel, address, nearby_landmarks, corporation, founding_time, state) VALUES
('10001', 'HC物业公司', '13800138000', '青海省西宁市城西区国投广场', '国投广场', '张三', '2010-01-01', '48001');

INSERT INTO store_user (store_user_id, store_id, user_id, rel_cd) VALUES
('2000000001', '10001', '1000000001', '600311000001'),
('2000000002', '10001', '1000000002', '600311000002');

INSERT INTO community (community_id, name, address, city_code, city_name, map_x, map_y, nearby_landmarks, tel, pay_fee_month, fee_price, state, store_id) VALUES
('2022081539020475', '测试小区', '青海省西宁市城西区国投广场', '630104', '青海省西宁市城西区', '101.33', '36.62', '国投广场', '18909711443', 12, 0, '1100', '10001');

INSERT INTO f_floor (floor_id, floor_num, name, community_id, floor_area, seq) VALUES
('3022081500000001', '1', '1号楼', '2022081539020475', 3200.00, 1);

INSERT INTO building_unit (unit_id, unit_num, floor_id, layer_count, lift, unit_area) VALUES
('4022081500000001', '1', '3022081500000001', 6, '1010', 800.00);

INSERT INTO building_room (room_id, room_num, unit_id, community_id, layer, apartment, built_up_area, room_area, state) VALUES
('5022081500000001', '101', '4022081500000001', '2022081539020475', '1', '两室一厅', 89.50, 78.00, '2002'),
('5022081500000002', '102', '4022081500000001', '2022081539020475', '1', '三室两厅', 120.00, 105.00, '2001');

INSERT INTO building_owner (member_id, owner_id, name, sex, link, id_card, owner_type_cd, person_role, community_id, address, state) VALUES
('6022081500000001', '6022081500000001', '李明', '0', '13800001111', '630104199001011234', '1001', '1', '2022081539020475', '1号楼1单元101', '2000');

INSERT INTO building_owner_room_rel (rel_id, owner_id, room_id, state, start_time) VALUES
('7022081500000001', '6022081500000001', '5022081500000001', '2002', NOW());

INSERT INTO parking_space (ps_id, num, community_id, parking_type, state, area) VALUES
('8022081500000001', 'A-001', '2022081539020475', '1', 'S', 12.00),
('8022081500000002', 'A-002', '2022081539020475', '1', 'F', 12.00);

INSERT INTO owner_car (car_id, owner_id, community_id, car_num, car_brand, car_color, ps_id, start_time, end_time) VALUES
('9022081500000001', '6022081500000001', '2022081539020475', '青A12345', '大众', '黑色', '8022081500000001', NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR));

INSERT INTO pay_fee_config (config_id, community_id, fee_type_cd, fee_name, fee_flag, square_price, additional_amount, payment_cycle, start_time, end_time) VALUES
('A022081500000001', '2022081539020475', '888800010001', '物业费', '1003006', 1.50, 0, '12', NOW(), DATE_ADD(NOW(), INTERVAL 10 YEAR));

INSERT INTO pay_fee (fee_id, config_id, community_id, payer_obj_id, payer_obj_type, fee_name, amount, state, start_time, end_time) VALUES
('B022081500000001', 'A022081500000001', '2022081539020475', '5022081500000001', '3333', '物业费', 134.25, '2008001', NOW(), DATE_ADD(NOW(), INTERVAL 1 MONTH));

INSERT INTO account (acct_id, acct_name, obj_id, community_id, amount) VALUES
('C022081500000001', '李明账户', '6022081500000001', '2022081539020475', 500.00);

CREATE TABLE tt_org (
    org_id VARCHAR(30) NOT NULL PRIMARY KEY,
    org_name VARCHAR(64) NOT NULL,
    parent_id VARCHAR(30) DEFAULT '-1',
    org_level VARCHAR(12) DEFAULT '1',
    community_id VARCHAR(30) DEFAULT NULL,
    description VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tt_notice (
    notice_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    title VARCHAR(128) NOT NULL,
    notice_type VARCHAR(12) DEFAULT '1002' COMMENT '1001通知 1002公告',
    context VARCHAR(2000) DEFAULT NULL,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '2000' COMMENT '1000草稿 2000已发布',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tt_vote (
    vote_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    title VARCHAR(128) NOT NULL,
    vote_type VARCHAR(12) DEFAULT '1002' COMMENT '1001问卷 1002投票',
    context VARCHAR(2000) DEFAULT NULL,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '2000' COMMENT '1000未开始 2000进行中 3000已结束',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tt_visit (
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
    state VARCHAR(12) DEFAULT '1000' COMMENT '1000待到访 2000在访 3000已离开',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tt_inspection (
    task_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    plan_name VARCHAR(64) NOT NULL,
    point_name VARCHAR(128) DEFAULT NULL,
    staff_name VARCHAR(64) DEFAULT NULL,
    inspect_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1000' COMMENT '1000待巡检 2000已巡检 3000异常',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tt_purchase (
    apply_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    resource_name VARCHAR(64) NOT NULL,
    spec VARCHAR(64) DEFAULT NULL,
    quantity DECIMAL(10,2) DEFAULT 1,
    price DECIMAL(10,2) DEFAULT 0,
    apply_user VARCHAR(64) DEFAULT NULL,
    state VARCHAR(12) DEFAULT '1000' COMMENT '1000待审核 2000已通过 3000已拒绝 4000已入库',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tt_contract (
    contract_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    contract_code VARCHAR(64) NOT NULL,
    contract_name VARCHAR(128) NOT NULL,
    contract_type VARCHAR(12) DEFAULT '1001' COMMENT '1001物业 1002租赁',
    party_a VARCHAR(64) DEFAULT NULL,
    party_b VARCHAR(64) DEFAULT NULL,
    amount DECIMAL(12,2) DEFAULT 0,
    start_time DATETIME DEFAULT NULL,
    end_time DATETIME DEFAULT NULL,
    state VARCHAR(12) DEFAULT '2000' COMMENT '1000待生效 2000履行中 3000已到期',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tt_discount (
    discount_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    discount_name VARCHAR(64) NOT NULL,
    discount_type VARCHAR(12) DEFAULT '1001' COMMENT '1001打折 2002减免',
    spec_value DECIMAL(10,2) DEFAULT 0,
    state VARCHAR(12) DEFAULT '1000' COMMENT '1000有效 2000失效',
    remark VARCHAR(200) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO building_room (room_id, room_num, unit_id, community_id, layer, apartment, built_up_area, room_area, state, room_sub_type) VALUES
('5022081500000003', 'S101', '4022081500000001', '2022081539020475', '1', '商铺', 45.00, 40.00, '2002', '119');

INSERT INTO tt_org (org_id, org_name, parent_id, org_level, community_id, description) VALUES
('ORG0000000000001', 'HC物业公司', '-1', '1', '2022081539020475', '总部'),
('ORG0000000000002', '客服中心', 'ORG0000000000001', '2', '2022081539020475', '业主服务'),
('ORG0000000000003', '工程部', 'ORG0000000000001', '2', '2022081539020475', '维修巡检');

INSERT INTO tt_notice (notice_id, community_id, title, notice_type, context, start_time, end_time, state) VALUES
('N022081500000001', '2022081539020475', '停水通知', '1001', '明日 9:00-12:00 市政检修停水，请提前储水。', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), '2000'),
('N022081500000002', '2022081539020475', '小区环境整治公告', '1002', '本周六开展公共区域清洁，请将楼道杂物自行收回。', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), '2000');

INSERT INTO tt_vote (vote_id, community_id, title, vote_type, context, start_time, end_time, state) VALUES
('V022081500000001', '2022081539020475', '是否增设充电车位', '1002', '拟在地下车库增设 8 个充电车位，请业主投票。', NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), '2000');

INSERT INTO tt_visit (visit_id, community_id, name, phone, car_num, visit_time, reason, owner_name, room_name, state) VALUES
('I022081500000001', '2022081539020475', '王强', '13900002222', '青A88888', NOW(), '探访亲友', '李明', '1-1-101', '2000');

INSERT INTO tt_inspection (task_id, community_id, plan_name, point_name, staff_name, inspect_time, state, remark) VALUES
('P022081500000001', '2022081539020475', '日常安防巡检', '1号楼大堂', '吴学文', NOW(), '2000', '设备正常'),
('P022081500000002', '2022081539020475', '消防巡检', '地下车库消火栓', NULL, DATE_ADD(NOW(), INTERVAL 1 DAY), '1000', NULL);

INSERT INTO tt_purchase (apply_id, community_id, resource_name, spec, quantity, price, apply_user, state, remark) VALUES
('U022081500000001', '2022081539020475', 'LED节能灯', '18W', 50, 12.50, '吴学文', '1000', '楼道更换');

INSERT INTO tt_contract (contract_id, community_id, contract_code, contract_name, contract_type, party_a, party_b, amount, start_time, end_time, state) VALUES
('T022081500000001', '2022081539020475', 'WY-2026-001', '物业服务合同', '1001', '业主委员会', 'HC物业公司', 120000.00, NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), '2000');

INSERT INTO tt_discount (discount_id, community_id, discount_name, discount_type, spec_value, state, remark) VALUES
('D022081500000001', '2022081539020475', '预缴一年九折', '1001', 0.90, '1000', '一次性预缴12个月'),
('D022081500000002', '2022081539020475', '困难户减免', '2002', 50.00, '1000', '每月减免50元');

CREATE TABLE tt_community_setting (
    setting_id VARCHAR(30) NOT NULL PRIMARY KEY,
    community_id VARCHAR(30) NOT NULL,
    setting_group VARCHAR(32) DEFAULT 'fee',
    setting_key VARCHAR(64) NOT NULL,
    setting_name VARCHAR(64) DEFAULT NULL,
    setting_value VARCHAR(255) DEFAULT NULL,
    remark VARCHAR(255) DEFAULT NULL,
    status_cd VARCHAR(2) NOT NULL DEFAULT '0',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO tt_community_setting (setting_id, community_id, setting_group, setting_key, setting_name, setting_value, remark) VALUES
('S001', '2022081539020475', 'fee', 'offlineCashier', '线下收银开关', '1', '缴费页面是否显示线下收费按钮，1开启 2关闭'),
('S002', '2022081539020475', 'fee', 'receiptStartNo', '收据开始编号', '057', '生成收据的起始编号，自动递增'),
('S003', '2022081539020475', 'fee', 'receivedAmountSwitch', '实收数开关', '1', '是否启用实收金额输入框，1开启 2关闭'),
('S004', '2022081539020475', 'pay', 'alipaySwitch', '支付宝支付', '2', '1开启 2关闭'),
('S005', '2022081539020475', 'msg', 'msgPush', '消息推送', '1', '1开启 2关闭'),
('S006', '2022081539020475', 'discount', 'discountRatio', '折扣比例', '0.9', '预缴折扣比例'),
('S007', '2022081539020475', 'points', 'maxPoints', '最大使用积分数', '100', '单次缴费最多使用积分'),
('S008', '2022081539020475', 'purchase', 'emergencyCount', '紧急采购次数', '3', '每月紧急采购上限'),
('S009', '2022081539020475', 'repair', 'repairSwitch', '维修工单', '1', '1开启 2关闭'),
('S010', '2022081539020475', 'refund', 'refundReceipt', '退费收据开关', '1', '1开启 2关闭'),
('S011', '2022081539020475', 'sms', 'aliSms', '阿里短信', '2', '1开启 2关闭');

-- 更丰富的演示数据（楼栋/房屋/业主/费用/工单等）见 seed_demo.sql
-- 空库初始化建议：先执行本文件，再执行 seed_demo.sql
