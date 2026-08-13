<template>
  <div class="screen">
    <div class="screen-hero glass">
      <div>
        <div class="page-kicker">{{ community?.name || "测试小区" }}</div>
        <h1>物业运营驾驶舱</h1>
      </div>
      <div class="screen-time">{{ now }}</div>
    </div>
    <div class="stats">
      <div class="stat-card" v-for="c in cards" :key="c.key">
        <div class="num">{{ stats[c.key] || 0 }}</div>
        <div class="label">{{ c.label }}</div>
      </div>
    </div>
    <el-row :gutter="12">
      <el-col :span="12">
        <div class="panel">
          <h3>收费汇总</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="应收">¥ {{ Number(summary.receivable || 0).toFixed(2) }}</el-descriptions-item>
            <el-descriptions-item label="欠费">¥ {{ Number(summary.arrears || 0).toFixed(2) }}</el-descriptions-item>
            <el-descriptions-item label="实收">¥ {{ Number(summary.received || 0).toFixed(2) }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="panel">
          <h3>工单</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="报修待处理">{{ work.repair?.pending || 0 }}</el-descriptions-item>
            <el-descriptions-item label="投诉待处理">{{ work.complaint?.pending || 0 }}</el-descriptions-item>
            <el-descriptions-item label="报修已完成">{{ work.repair?.done || 0 }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import http from "@/api/http";
import { getCommunityId } from "@/utils/community";

const stats = ref({});
const summary = ref({});
const work = ref({});
const community = ref(null);
const now = ref("");
let timer;

onMounted(async () => {
  const tick = () => {
    now.value = new Date().toLocaleString("zh-CN", { hour12: false });
  };
  tick();
  timer = setInterval(tick, 1000);
  const cid = getCommunityId();
  const [s, c, f, w] = await Promise.all([
    http.get("/dashboard.stats"),
    http.get("/community.listCommunitys", { params: { page: 1, row: 1 } }),
    http.get("/report.feeSummary", { params: { communityId: cid } }),
    http.get("/report.workSummary", { params: { communityId: cid } })
  ]);
  stats.value = s.data || {};
  community.value = (c.data && c.data[0]) || null;
  summary.value = f.data || {};
  work.value = w.data || {};
});
onUnmounted(() => clearInterval(timer));

const cards = [
  { key: "roomCount", label: "房屋" },
  { key: "ownerCount", label: "业主" },
  { key: "feeCount", label: "费用单" },
  { key: "repairCount", label: "报修" },
  { key: "visitCount", label: "访客" },
  { key: "noticeCount", label: "公告" },
  { key: "complaintCount", label: "投诉" },
  { key: "inspectionCount", label: "巡检" }
];
</script>
