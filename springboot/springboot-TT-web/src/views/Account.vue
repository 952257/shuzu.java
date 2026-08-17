<template>
  <div class="page-wrap">
    <div class="page-hero">
      <div>
        <div class="page-kicker">业主资金</div>
        <h2>业主账户</h2>
        <p>点击账户查看明细。预存会增加余额，撤销会回退对应流水。</p>
      </div>
      <div class="welcome-actions">
        <el-input-number v-model="amount" :min="1" :step="50" />
        <el-button type="primary" @click="load">刷新账户</el-button>
      </div>
    </div>
    <el-row :gutter="16">
      <el-col :span="8" v-for="item in list" :key="item.acctId">
        <div class="panel account-card" @click="loadDetail(item.acctId)">
          <div class="account-top">
            <div>
              <div class="muted">{{ item.acctName }}</div>
              <div class="money" style="font-size: 28px">¥ {{ Number(item.amount || 0).toFixed(2) }}</div>
            </div>
            <el-tag type="success" effect="light">现金账户</el-tag>
          </div>
          <div class="kv-list">
            <div class="kv-row"><span class="kv-label">业主ID</span><span class="kv-value">{{ item.objId }}</span></div>
            <div class="kv-row"><span class="kv-label">小区ID</span><span class="kv-value">{{ item.communityId }}</span></div>
          </div>
          <div class="toolbar" style="margin: 8px 0 0">
            <el-button type="success" size="small" @click.stop="prestore(item.acctId)">预存 {{ amount }} 元</el-button>
            <el-button size="small" @click.stop="loadDetail(item.acctId)">查看明细</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
    <div class="panel">
      <div class="panel-head">
        <h3>账户明细 {{ currentAcct ? "· " + currentAcct : "" }}</h3>
      </div>
      <el-table :data="details" stripe empty-text="请选择一个账户查看明细">
        <el-table-column label="类型" width="140">
          <template #default="{ row }">
            <el-tag :type="(DETAIL_TYPE[row.detailType] || {}).type">{{ (DETAIL_TYPE[row.detailType] || {}).label || row.detailType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="140">
          <template #default="{ row }">
            <span class="money">¥ {{ Number(row.amount || 0).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="(DETAIL_STATE[row.state] || {}).type">{{ (DETAIL_STATE[row.state] || {}).label || row.state }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="180" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button v-if="row.state !== '2002'" link type="danger" @click="cancel(row.detailId, row.acctId)">撤销</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import http from "@/api/http";
import { DETAIL_STATE, DETAIL_TYPE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";

const list = ref([]);
const details = ref([]);
const amount = ref(100);
const currentAcct = ref("");

const load = async () => {
  const res = await http.get("/account.queryOwnerAccount", { params: { communityId: getCommunityId(), page: 1, row: 20 } });
  if (res.code === 0) {
    list.value = res.data || [];
    if (list.value[0]) {
      loadDetail(list.value[0].acctId);
    }
  }
};

const loadDetail = async (acctId) => {
  currentAcct.value = acctId;
  const res = await http.get("/account.listAccountDetail", { params: { acctId, page: 1, row: 50 } });
  if (res.code === 0) {
    details.value = res.data || [];
  }
};

const prestore = async (acctId) => {
  const res = await http.post("/account.ownerPrestoreAccount", { acctId, amount: amount.value, remark: "前台预存" });
  if (res.code === 0) {
    ElMessage.success("预存成功");
    load();
    loadDetail(acctId);
  }
};

const cancel = async (detailId, acctId) => {
  const res = await http.post("/account.cancelAccountDetail", { detailId });
  if (res.code === 0) {
    ElMessage.success("已撤销");
    load();
    loadDetail(acctId);
  }
};

onMounted(load);
</script>

<style scoped>
.account-card {
  cursor: pointer;
  margin-bottom: 16px;
}
.account-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}
</style>
