<template>
  <div class="page-wrap">
    <div class="page-hero">
      <div>
        <div class="page-kicker">工单报表</div>
        <h2>报修与投诉汇总</h2>
        <p>按处理状态统计工单量，便于值班安排。</p>
      </div>
      <el-button @click="load">刷新</el-button>
    </div>
    <el-row :gutter="16">
      <el-col :span="12">
        <div class="panel">
          <h3>报修</h3>
          <div class="kv-list">
            <div class="kv-row"><span class="kv-label">全部</span><span class="kv-value">{{ repair.total || 0 }}</span></div>
            <div class="kv-row"><span class="kv-label">待处理</span><span class="kv-value">{{ repair.pending || 0 }}</span></div>
            <div class="kv-row"><span class="kv-label">处理中</span><span class="kv-value">{{ repair.processing || 0 }}</span></div>
            <div class="kv-row"><span class="kv-label">已完成</span><span class="kv-value">{{ repair.done || 0 }}</span></div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="panel">
          <h3>投诉建议</h3>
          <div class="kv-list">
            <div class="kv-row"><span class="kv-label">全部</span><span class="kv-value">{{ complaint.total || 0 }}</span></div>
            <div class="kv-row"><span class="kv-label">待处理</span><span class="kv-value">{{ complaint.pending || 0 }}</span></div>
            <div class="kv-row"><span class="kv-label">处理中</span><span class="kv-value">{{ complaint.processing || 0 }}</span></div>
            <div class="kv-row"><span class="kv-label">已完成</span><span class="kv-value">{{ complaint.done || 0 }}</span></div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import http from "@/api/http";
import { getCommunityId } from "@/utils/community";

const repair = ref({});
const complaint = ref({});

const load = async () => {
  const res = await http.get("/report.workSummary", { params: { communityId: getCommunityId() } });
  repair.value = (res.data && res.data.repair) || {};
  complaint.value = (res.data && res.data.complaint) || {};
};

onMounted(load);
</script>
