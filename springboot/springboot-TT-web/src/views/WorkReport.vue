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
          <el-descriptions :column="1" border>
            <el-descriptions-item label="全部">{{ repair.total || 0 }}</el-descriptions-item>
            <el-descriptions-item label="待处理">{{ repair.pending || 0 }}</el-descriptions-item>
            <el-descriptions-item label="处理中">{{ repair.processing || 0 }}</el-descriptions-item>
            <el-descriptions-item label="已完成">{{ repair.done || 0 }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="panel">
          <h3>投诉建议</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="全部">{{ complaint.total || 0 }}</el-descriptions-item>
            <el-descriptions-item label="待处理">{{ complaint.pending || 0 }}</el-descriptions-item>
            <el-descriptions-item label="处理中">{{ complaint.processing || 0 }}</el-descriptions-item>
            <el-descriptions-item label="已完成">{{ complaint.done || 0 }}</el-descriptions-item>
          </el-descriptions>
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
