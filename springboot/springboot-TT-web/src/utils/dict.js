export const ROLE = {
  ADMIN: { label: "管理员", type: "danger" },
  STAFF: { label: "员工", type: "primary" }
};

export const COMMUNITY_STATE = {
  1100: { label: "审核完成", type: "success" },
  1000: { label: "待审核", type: "warning" }
};

export const ROOM_STATE = {
  2001: { label: "未售", type: "info" },
  2002: { label: "已入住", type: "success" }
};

export const LIFT = {
  1010: { label: "有电梯", type: "success" },
  2020: { label: "无电梯", type: "info" }
};

export const PARKING_TYPE = {
  1: { label: "地上", type: "success" },
  2: { label: "地下", type: "warning" }
};

export const PARKING_STATE = {
  F: { label: "空闲", type: "success" },
  S: { label: "已售", type: "danger" },
  H: { label: "已出租", type: "warning" }
};

export const AUTH_STATE = {
  10000: { label: "待审核", type: "warning" },
  12000: { label: "已通过", type: "success" },
  13000: { label: "已拒绝", type: "danger" }
};

export const FEE_STATE = {
  2008001: { label: "收费中", type: "warning" },
  2009001: { label: "已结束", type: "info" }
};

export const PAY_STATE = {
  1400: { label: "正常", type: "success" },
  1500: { label: "退费", type: "danger" }
};

export const METER_TYPE = {
  2020: { label: "水表", type: "primary" },
  3030: { label: "电表", type: "warning" }
};

export const REPAIR_STATE = {
  1000: { label: "待处理", type: "warning" },
  1100: { label: "处理中", type: "primary" },
  1200: { label: "已完成", type: "success" },
  1300: { label: "已评价", type: "info" }
};

export const COMPLAINT_TYPE = {
  809001: { label: "投诉", type: "danger" },
  809002: { label: "建议", type: "success" }
};

export const COMPLAINT_STATE = {
  10001: { label: "待处理", type: "warning" },
  10002: { label: "处理中", type: "primary" },
  10003: { label: "已完成", type: "success" }
};

export const DETAIL_TYPE = {
  1001: { label: "转入", type: "success" },
  2002: { label: "转出", type: "warning" },
  3003: { label: "撤销", type: "info" }
};

export const DETAIL_STATE = {
  1001: { label: "正常", type: "success" },
  2002: { label: "已撤销", type: "info" }
};

export const SEX = {
  0: { label: "男", type: "primary" },
  1: { label: "女", type: "danger" }
};

export const PERSON_ROLE = {
  1: { label: "业主", type: "success" },
  2: { label: "租客", type: "warning" },
  3: { label: "家庭成员", type: "info" }
};

export const ROOM_SUB_TYPE = {
  110: { label: "住宅", type: "success" },
  119: { label: "商铺", type: "warning" }
};

export const NOTICE_TYPE = {
  1001: { label: "通知", type: "warning" },
  1002: { label: "公告", type: "primary" }
};

export const NOTICE_STATE = {
  1000: { label: "草稿", type: "info" },
  2000: { label: "已发布", type: "success" }
};

export const VOTE_TYPE = {
  1001: { label: "问卷", type: "primary" },
  1002: { label: "投票", type: "success" }
};

export const VOTE_STATE = {
  1000: { label: "未开始", type: "info" },
  2000: { label: "进行中", type: "warning" },
  3000: { label: "已结束", type: "success" }
};

export const VISIT_STATE = {
  1000: { label: "待到访", type: "info" },
  2000: { label: "在访", type: "warning" },
  3000: { label: "已离开", type: "success" }
};

export const INSPECT_STATE = {
  1000: { label: "待巡检", type: "warning" },
  2000: { label: "已巡检", type: "success" },
  3000: { label: "异常", type: "danger" }
};

export const PURCHASE_STATE = {
  1000: { label: "待审核", type: "warning" },
  2000: { label: "已通过", type: "success" },
  3000: { label: "已拒绝", type: "danger" },
  4000: { label: "已入库", type: "primary" }
};

export const CONTRACT_TYPE = {
  1001: { label: "物业合同", type: "primary" },
  1002: { label: "租赁合同", type: "warning" }
};

export const CONTRACT_STATE = {
  1000: { label: "待生效", type: "info" },
  2000: { label: "履行中", type: "success" },
  3000: { label: "已到期", type: "danger" }
};

export const DISCOUNT_TYPE = {
  1001: { label: "打折", type: "success" },
  2002: { label: "减免", type: "warning" }
};

export const DISCOUNT_STATE = {
  1000: { label: "有效", type: "success" },
  2000: { label: "失效", type: "info" }
};

export const AUDIT_STATE = {
  1000: { label: "待审核", type: "warning" },
  1100: { label: "已通过", type: "success" },
  1200: { label: "已拒绝", type: "danger" }
};
