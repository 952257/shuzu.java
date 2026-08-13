USE TT;

CREATE TABLE IF NOT EXISTS tt_community_setting (
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

INSERT IGNORE INTO tt_community_setting (setting_id, community_id, setting_group, setting_key, setting_name, setting_value, remark) VALUES
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
