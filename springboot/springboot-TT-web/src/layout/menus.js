export const catalogs = [
  {
    id: "community",
    title: "小区",
    icon: "OfficeBuilding",
    children: [
      { path: "/community", title: "小区信息" },
      { path: "/property", title: "物业公司" },
      { path: "/org", title: "组织架构" },
      { path: "/staff", title: "员工管理" }
    ]
  },
  {
    id: "house",
    title: "房产",
    icon: "House",
    children: [
      { path: "/floor", title: "楼栋管理" },
      { path: "/unit", title: "单元管理" },
      { path: "/room", title: "房屋管理" },
      { path: "/shop", title: "商铺管理" },
      { path: "/owner", title: "业主信息" },
      { path: "/member", title: "家庭成员" },
      { path: "/auth", title: "房屋认证" }
    ]
  },
  {
    id: "fee",
    title: "费用",
    icon: "Wallet",
    children: [
      { path: "/fee-config", title: "费用项" },
      { path: "/fee", title: "费用账单" },
      { path: "/arrears", title: "欠费催缴" },
      { path: "/fee-detail", title: "缴费记录" },
      { path: "/fee-audit", title: "缴费审核" },
      { path: "/receipt", title: "打印收据" },
      { path: "/meter", title: "水电抄表" },
      { path: "/account", title: "业主账户" }
    ]
  },
  {
    id: "park",
    title: "停车",
    icon: "Position",
    children: [
      { path: "/parking", title: "车位管理" },
      { path: "/car", title: "车辆管理" },
      { path: "/visit", title: "访客登记" }
    ]
  },
  {
    id: "repair",
    title: "报修",
    icon: "Ticket",
    children: [
      { path: "/repair", title: "报修工单" },
      { path: "/complaint", title: "投诉建议" }
    ]
  },
  {
    id: "inspect",
    title: "巡检",
    icon: "Aim",
    children: [{ path: "/inspection", title: "巡检任务" }]
  },
  {
    id: "purchase",
    title: "采购",
    icon: "ShoppingCart",
    children: [{ path: "/purchase", title: "采购申请" }]
  },
  {
    id: "office",
    title: "办公",
    icon: "Briefcase",
    children: [
      { path: "/notice", title: "公告通知" },
      { path: "/vote", title: "问卷投票" },
      { path: "/staff", title: "员工信息" }
    ]
  },
  {
    id: "contract",
    title: "合同",
    icon: "Document",
    children: [{ path: "/contract", title: "合同管理" }]
  },
  {
    id: "report",
    title: "报表",
    icon: "DataAnalysis",
    children: [
      { path: "/fee-report", title: "费用报表" },
      { path: "/work-report", title: "工单报表" }
    ]
  },
  {
    id: "discount",
    title: "优惠",
    icon: "Present",
    children: [{ path: "/discount", title: "缴费折扣" }]
  },
  {
    id: "system",
    title: "系统",
    icon: "Setting",
    children: [
      { path: "/settings", title: "小区配置" },
      { path: "/password", title: "修改密码" },
      { path: "/logs", title: "操作日志" },
      { path: "/property", title: "商户信息" },
      { path: "/receipt", title: "收据模板" }
    ]
  }
];

export const quickMenus = [
  { path: "/staff", title: "员工信息", icon: "User" },
  { path: "/screen", title: "小区大屏", icon: "Monitor" },
  { path: "/desk", title: "业务受理", icon: "Ticket" },
  { path: "/settings", title: "小区配置", icon: "Setting" }
];

export function catalogOf(path) {
  return catalogs.find((c) => c.children.some((item) => item.path === path));
}
