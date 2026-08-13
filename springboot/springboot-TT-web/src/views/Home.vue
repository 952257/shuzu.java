<template>
  <div class="home">
    <div class="welcome">
      <div>
        <div class="page-kicker">{{ greeting }}，{{ userName }}</div>
        <h2>今日小区运营一览</h2>
        <p>先完善房产档案，再绑定业主与车位，最后创建费用并处理工单。</p>
      </div>
      <div class="welcome-actions">
        <el-button type="primary" @click="$router.push('/room')">去交房</el-button>
        <el-button @click="$router.push('/arrears')">欠费催缴</el-button>
        <el-button @click="$router.push('/repair')">看报修</el-button>
        <el-button @click="$router.push('/notice')">发公告</el-button>
      </div>
    </div>

    <div class="stats">
      <div class="stat-card" v-for="c in cards" :key="c.key" @click="$router.push(c.to)">
        <div class="stat-icon" :style="{ background: c.bg, color: c.color }">
          <el-icon :size="22"><component :is="c.icon" /></el-icon>
        </div>
        <div>
          <div class="num">{{ stats[c.key] || 0 }}</div>
          <div class="label">{{ c.label }}</div>
        </div>
      </div>
    </div>

    <el-row :gutter="16">
      <el-col :span="16">
        <div class="panel">
          <div class="panel-head">
            <h3>待办工单</h3>
            <el-button link type="primary" @click="$router.push('/repair')">全部报修</el-button>
          </div>
          <el-table :data="repairs" size="small" stripe empty-text="暂无报修">
            <el-table-column prop="repairName" label="报修人" width="90" />
            <el-table-column prop="repairObjName" label="位置" width="110" />
            <el-table-column prop="context" label="内容" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="repairTag(row.state).type" size="small">{{ repairTag(row.state).label }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <div class="panel-head" style="margin-top: 18px">
            <h3>最新投诉</h3>
            <el-button link type="primary" @click="$router.push('/complaint')">全部投诉</el-button>
          </div>
          <el-table :data="complaints" size="small" stripe empty-text="暂无投诉">
            <el-table-column prop="complaintName" label="投诉人" width="90" />
            <el-table-column label="类型" width="90">
              <template #default="{ row }">
                <el-tag :type="row.typeCd === '809002' ? 'success' : 'danger'" size="small">
                  {{ row.typeCd === "809002" ? "建议" : "投诉" }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="context" label="内容" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="complaintTag(row.state).type" size="small">{{ complaintTag(row.state).label }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <div class="panel-head" style="margin-top: 18px">
            <h3>最新公告</h3>
            <el-button link type="primary" @click="$router.push('/notice')">全部公告</el-button>
          </div>
          <el-table :data="notices" size="small" stripe empty-text="暂无公告">
            <el-table-column prop="title" label="标题" width="160" />
            <el-table-column prop="context" label="内容" show-overflow-tooltip />
          </el-table>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="panel">
          <h3>小区档案</h3>
          <el-descriptions :column="1" border size="small" v-if="community">
            <el-descriptions-item label="名称">{{ community.name }}</el-descriptions-item>
            <el-descriptions-item label="地址">{{ community.address }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ community.tel }}</el-descriptions-item>
            <el-descriptions-item label="地标">{{ community.nearbyLandmarks }}</el-descriptions-item>
            <el-descriptions-item label="收费周期">{{ community.payFeeMonth }} 个月</el-descriptions-item>
          </el-descriptions>
          <h3 style="margin-top: 18px">房屋入住</h3>
          <el-progress :percentage="soldRate" :stroke-width="12" striped striped-flow />
          <p class="muted">已入住 {{ sold }} / 共 {{ rooms.length }} 套</p>
          <h3 style="margin-top: 18px">收费状态</h3>
          <el-progress :percentage="feeOpenRate" status="warning" :stroke-width="12" />
          <p class="muted">收费中 {{ feeOpen }} 笔 · 已结束 {{ feeClosed }} 笔</p>
        </div>
        <div class="panel" style="margin-top: 16px">
          <h3>标准作业顺序</h3>
          <el-steps direction="vertical" :active="3" finish-status="success">
            <el-step title="建物业与小区" description="维护公司和小区基础信息" />
            <el-step title="录楼栋房屋" description="楼栋 → 单元 → 房屋 / 车位" />
            <el-step title="绑定业主" description="添加业主后交房、认证审核" />
            <el-step title="收费与工单" description="账单、催缴、抄表、报修投诉" />
            <el-step title="运营巡检" description="公告投票、访客、巡检采购" />
          </el-steps>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { Bell, Coin, House, OfficeBuilding, Position, Ticket, User, Warning } from "@element-plus/icons-vue";
import http from "@/api/http";
import { COMPLAINT_STATE, REPAIR_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";

const stats = ref({});
const community = ref(null);
const rooms = ref([]);
const repairs = ref([]);
const complaints = ref([]);
const fees = ref([]);
const notices = ref([]);
const userName = computed(() => JSON.parse(localStorage.getItem("tt_user") || "{}").userName || "同事");
const hour = new Date().getHours();
const greeting = hour < 12 ? "上午好" : hour < 18 ? "下午好" : "晚上好";
const cid = getCommunityId();

const cards = [
  { key: "communityCount", label: "小区", to: "/community", icon: OfficeBuilding, bg: "#e8f6f5", color: "#1aa39a" },
  { key: "roomCount", label: "房屋", to: "/room", icon: House, bg: "#eef3ff", color: "#3b82f6" },
  { key: "ownerCount", label: "业主", to: "/owner", icon: User, bg: "#f3e8ff", color: "#8b5cf6" },
  { key: "feeCount", label: "费用单", to: "/fee", icon: Coin, bg: "#fff7e6", color: "#f59e0b" },
  { key: "repairCount", label: "报修", to: "/repair", icon: Ticket, bg: "#ffe8e8", color: "#ef4444" },
  { key: "complaintCount", label: "投诉", to: "/complaint", icon: Warning, bg: "#e8fff4", color: "#10b981" },
  { key: "visitCount", label: "访客", to: "/visit", icon: Position, bg: "#e0f2fe", color: "#0284c7" },
  { key: "noticeCount", label: "公告", to: "/notice", icon: Bell, bg: "#fce7f3", color: "#db2777" }
];

const sold = computed(() => rooms.value.filter((r) => r.state === "2002").length);
const soldRate = computed(() => (rooms.value.length ? Math.round((sold.value / rooms.value.length) * 100) : 0));
const feeOpen = computed(() => fees.value.filter((f) => f.state === "2008001").length);
const feeClosed = computed(() => fees.value.filter((f) => f.state === "2009001").length);
const feeOpenRate = computed(() => (fees.value.length ? Math.round((feeOpen.value / fees.value.length) * 100) : 0));

const repairTag = (v) => REPAIR_STATE[v] || { label: v, type: "info" };
const complaintTag = (v) => COMPLAINT_STATE[v] || { label: v, type: "info" };

onMounted(async () => {
  const [s, c, r, rp, cp, f, n] = await Promise.all([
    http.get("/dashboard.stats"),
    http.get("/community.listCommunitys", { params: { page: 1, row: 1 } }),
    http.get("/room.queryRooms", { params: { communityId: cid, page: 1, row: 50 } }),
    http.get("/repair.listRepairs", { params: { communityId: cid, page: 1, row: 5 } }),
    http.get("/complaint.listComplaints", { params: { communityId: cid, page: 1, row: 5 } }),
    http.get("/fee.listFee", { params: { communityId: cid, page: 1, row: 50 } }),
    http.get("/notice.listNotices", { params: { communityId: cid, page: 1, row: 5 } })
  ]);
  stats.value = s.data || {};
  community.value = (c.data && c.data[0]) || null;
  rooms.value = r.data || [];
  repairs.value = rp.data || [];
  complaints.value = cp.data || [];
  fees.value = f.data || [];
  notices.value = n.data || [];
});
</script>
