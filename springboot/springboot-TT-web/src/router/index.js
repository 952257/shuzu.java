import { createRouter, createWebHistory } from "vue-router";

const routes = [
  { path: "/login", name: "login", component: () => import("@/views/Login.vue") },
  {
    path: "/",
    component: () => import("@/layout/MainLayout.vue"),
    redirect: "/home",
    children: [
      { path: "home", name: "home", meta: { title: "工作台" }, component: () => import("@/views/Home.vue") },
      { path: "desk", name: "desk", meta: { title: "业务受理" }, component: () => import("@/views/BusinessDesk.vue") },
      { path: "screen", name: "screen", meta: { title: "小区大屏" }, component: () => import("@/views/Screen.vue") },
      { path: "settings", name: "settings", meta: { title: "小区配置" }, component: () => import("@/views/Settings.vue") },
      { path: "password", name: "password", meta: { title: "修改密码" }, component: () => import("@/views/Password.vue") },
      { path: "logs", name: "logs", meta: { title: "操作日志" }, component: () => import("@/views/Logs.vue") },
      { path: "staff", name: "staff", meta: { title: "员工管理" }, component: () => import("@/views/Staff.vue") },
      { path: "org", name: "org", meta: { title: "组织架构" }, component: () => import("@/views/Org.vue") },
      { path: "property", name: "property", meta: { title: "物业公司" }, component: () => import("@/views/Property.vue") },
      { path: "community", name: "community", meta: { title: "小区信息" }, component: () => import("@/views/Community.vue") },
      { path: "floor", name: "floor", meta: { title: "楼栋管理" }, component: () => import("@/views/Floor.vue") },
      { path: "unit", name: "unit", meta: { title: "单元管理" }, component: () => import("@/views/Unit.vue") },
      { path: "room", name: "room", meta: { title: "房屋管理" }, component: () => import("@/views/Room.vue") },
      { path: "shop", name: "shop", meta: { title: "商铺管理" }, component: () => import("@/views/Shop.vue") },
      { path: "parking", name: "parking", meta: { title: "车位管理" }, component: () => import("@/views/Parking.vue") },
      { path: "car", name: "car", meta: { title: "车辆管理" }, component: () => import("@/views/Car.vue") },
      { path: "visit", name: "visit", meta: { title: "访客登记" }, component: () => import("@/views/Visit.vue") },
      { path: "owner", name: "owner", meta: { title: "业主信息" }, component: () => import("@/views/Owner.vue") },
      { path: "member", name: "member", meta: { title: "家庭成员" }, component: () => import("@/views/Member.vue") },
      { path: "auth", name: "auth", meta: { title: "房屋认证" }, component: () => import("@/views/Auth.vue") },
      { path: "account", name: "account", meta: { title: "业主账户" }, component: () => import("@/views/Account.vue") },
      { path: "fee-config", name: "feeConfig", meta: { title: "费用项" }, component: () => import("@/views/FeeConfig.vue") },
      { path: "fee", name: "fee", meta: { title: "费用账单" }, component: () => import("@/views/Fee.vue") },
      { path: "arrears", name: "arrears", meta: { title: "欠费催缴" }, component: () => import("@/views/Arrears.vue") },
      { path: "fee-detail", name: "feeDetail", meta: { title: "缴费记录" }, component: () => import("@/views/FeeDetail.vue") },
      { path: "fee-audit", name: "feeAudit", meta: { title: "缴费审核" }, component: () => import("@/views/FeeAudit.vue") },
      { path: "receipt", name: "receipt", meta: { title: "打印收据" }, component: () => import("@/views/Receipt.vue") },
      { path: "meter", name: "meter", meta: { title: "水电抄表" }, component: () => import("@/views/Meter.vue") },
      { path: "discount", name: "discount", meta: { title: "缴费折扣" }, component: () => import("@/views/Discount.vue") },
      { path: "fee-report", name: "feeReport", meta: { title: "费用报表" }, component: () => import("@/views/FeeReport.vue") },
      { path: "repair", name: "repair", meta: { title: "报修工单" }, component: () => import("@/views/Repair.vue") },
      { path: "complaint", name: "complaint", meta: { title: "投诉建议" }, component: () => import("@/views/Complaint.vue") },
      { path: "inspection", name: "inspection", meta: { title: "巡检任务" }, component: () => import("@/views/Inspection.vue") },
      { path: "purchase", name: "purchase", meta: { title: "采购申请" }, component: () => import("@/views/Purchase.vue") },
      { path: "contract", name: "contract", meta: { title: "合同管理" }, component: () => import("@/views/Contract.vue") },
      { path: "work-report", name: "workReport", meta: { title: "工单报表" }, component: () => import("@/views/WorkReport.vue") },
      { path: "notice", name: "notice", meta: { title: "公告通知" }, component: () => import("@/views/Notice.vue") },
      { path: "vote", name: "vote", meta: { title: "问卷投票" }, component: () => import("@/views/Vote.vue") }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to) => {
  const token = localStorage.getItem("tt_token");
  if (to.path !== "/login" && !token) {
    return "/login";
  }
  if (to.path === "/login" && token) {
    return "/home";
  }
  return true;
});

export default router;
