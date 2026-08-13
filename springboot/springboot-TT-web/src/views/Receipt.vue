<template>
  <div class="page-wrap">
    <div class="page-hero">
      <div>
        <div class="page-kicker">收费管理</div>
        <h2>打印收据</h2>
        <p>选择已审核通过的缴费记录，打印收费收据。</p>
      </div>
    </div>
    <div class="panel crud-panel">
      <div class="toolbar">
        <el-input v-model="feeId" placeholder="费用ID" clearable style="width: 200px" @keyup.enter="load" />
        <el-button type="primary" @click="load">查询</el-button>
      </div>
      <el-table :data="list" v-loading="loading" stripe border empty-text="暂无已审核缴费">
        <el-table-column prop="detailId" label="收据号" min-width="160" />
        <el-table-column prop="feeId" label="费用ID" min-width="150" />
        <el-table-column prop="receivedAmount" label="实收" width="120">
          <template #default="{ row }">¥ {{ Number(row.receivedAmount || 0).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="payTime" label="缴费时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="print(row)">打印收据</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import http from "@/api/http";
import { getCommunityId } from "@/utils/community";

const list = ref([]);
const loading = ref(false);
const feeId = ref("");

const load = async () => {
  loading.value = true;
  try {
    const res = await http.get("/fee.queryFeeDetail", {
      params: { communityId: getCommunityId(), feeId: feeId.value || undefined, auditState: "1100", page: 1, row: 50 }
    });
    list.value = (res.data || []).filter((d) => d.state !== "1500");
  } finally {
    loading.value = false;
  }
};

const print = (row) => {
  const html = `<!DOCTYPE html><html><head><meta charset="utf-8"><title>收费收据</title>
  <style>body{font-family:sans-serif;padding:32px}h2{text-align:center}table{width:100%;border-collapse:collapse;margin-top:16px}td{border:1px solid #333;padding:8px}</style>
  </head><body>
  <h2>物业收费收据</h2>
  <table>
    <tr><td>收据号</td><td>${row.detailId}</td></tr>
    <tr><td>费用ID</td><td>${row.feeId}</td></tr>
    <tr><td>实收金额</td><td>¥ ${Number(row.receivedAmount || 0).toFixed(2)}</td></tr>
    <tr><td>应收金额</td><td>¥ ${Number(row.receivableAmount || 0).toFixed(2)}</td></tr>
    <tr><td>缴费时间</td><td>${row.payTime || ""}</td></tr>
    <tr><td>收款单位</td><td>HC物业公司</td></tr>
  </table>
  <p style="margin-top:40px;text-align:right">经办人：_____________</p>
  </body></html>`;
  const win = window.open("", "_blank", "width=720,height=640");
  win.document.write(html);
  win.document.close();
  win.focus();
  win.print();
};

onMounted(load);
</script>
