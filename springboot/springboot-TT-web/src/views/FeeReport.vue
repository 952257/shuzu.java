<template>
  <div class="page-wrap">
    <div class="page-hero">
      <div>
        <div class="page-kicker">收费报表</div>
        <h2>费用台账与汇总</h2>
        <p>按费用项汇总应收、欠费和实收，对应演示站费用台账 / 汇总表。</p>
      </div>
      <el-button @click="load">刷新</el-button>
    </div>
    <div class="stats">
      <div class="stat-card" v-for="c in cards" :key="c.label">
        <div>
          <div class="num">{{ c.value }}</div>
          <div class="label">{{ c.label }}</div>
        </div>
      </div>
    </div>
    <div class="panel crud-panel">
      <h3 style="margin: 0 0 12px">费用汇总表</h3>
      <el-table :data="items" stripe border empty-text="暂无费用数据">
        <el-table-column prop="feeName" label="费用项" />
        <el-table-column prop="billCount" label="账单数" width="120" />
        <el-table-column label="应收" width="140">
          <template #default="{ row }">¥ {{ Number(row.receivable || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="欠费" width="140">
          <template #default="{ row }">¥ {{ Number(row.arrears || 0).toFixed(2) }}</template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import http from "@/api/http";
import { getCommunityId } from "@/utils/community";

const summary = ref({ billCount: 0, openCount: 0, receivable: 0, arrears: 0, received: 0, items: [] });
const items = computed(() => summary.value.items || []);
const money = (v) => `¥ ${Number(v || 0).toFixed(2)}`;
const cards = computed(() => [
  { label: "账单数", value: summary.value.billCount || 0 },
  { label: "收费中", value: summary.value.openCount || 0 },
  { label: "应收合计", value: money(summary.value.receivable) },
  { label: "欠费合计", value: money(summary.value.arrears) },
  { label: "实收合计", value: money(summary.value.received) }
]);

const load = async () => {
  const res = await http.get("/report.feeSummary", { params: { communityId: getCommunityId() } });
  summary.value = res.data || summary.value;
};

onMounted(load);
</script>
