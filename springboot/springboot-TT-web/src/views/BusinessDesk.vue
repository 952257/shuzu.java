<template>
  <div class="desk">
    <div class="desk-toolbar glass-lite">
      <el-select v-model="searchType" style="width: 120px">
        <el-option label="房屋号" value="room" />
      </el-select>
      <el-input
        v-model="roomCode"
        placeholder="请输入房屋编号 楼栋-单元-房屋 如 1-1-101"
        style="width: 360px"
        @keyup.enter="search"
      />
      <el-button type="primary" @click="search">查询</el-button>
      <el-button @click="pickOpen = true">选择房屋</el-button>
    </div>

    <div class="owner-card glass-lite" v-if="desk">
      <div class="owner-avatar">{{ (owner.name || "业").slice(0, 1) }}</div>
      <div class="kv-grid">
        <div class="kv-row" v-for="item in ownerRows" :key="item.label">
          <span class="kv-label">{{ item.label }}</span>
          <span class="kv-value">
            <span v-if="item.money" class="money">{{ item.value }}</span>
            <template v-else>{{ item.value }}</template>
          </span>
        </div>
      </div>
    </div>
    <el-empty v-else description="请先查询房屋，例如 1-1-101" />

    <div class="desk-tabs glass-lite" v-if="desk">
      <el-tabs v-model="tab">
        <el-tab-pane label="房屋费用" name="fee" />
        <el-tab-pane label="停车费用" name="parkFee" />
        <el-tab-pane label="缴费历史" name="history" />
        <el-tab-pane label="结束费用" name="ended" />
        <el-tab-pane label="房屋" name="room" />
        <el-tab-pane label="业主商铺" name="shop" />
        <el-tab-pane label="家庭成员" name="member" />
        <el-tab-pane label="车辆" name="car" />
        <el-tab-pane label="催缴" name="urge" />
        <el-tab-pane label="业主反馈" name="feedback" />
        <el-tab-pane label="报修单" name="repair" />
        <el-tab-pane label="投诉单" name="complaint" />
        <el-tab-pane label="车辆同步" name="sync" />
        <el-tab-pane label="补打收据" name="receipt" />
        <el-tab-pane label="历史业主" name="oldOwner" />
        <el-tab-pane label="抄表记录" name="meter" />
      </el-tabs>
      <div class="desk-actions">
        <el-button type="primary" @click="batchPay">批量缴费</el-button>
        <el-button type="primary" @click="prestore">预存收款</el-button>
      </div>
      <el-table :data="tableData" size="small" stripe empty-text="暂无数据" v-loading="loading">
        <el-table-column v-for="col in columns" :key="col.prop" :prop="col.prop" :label="col.label" :min-width="col.width || 110" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tag v-if="col.map" :type="(col.map[row[col.prop]] || {}).type || 'info'" size="small">
              {{ (col.map[row[col.prop]] || {}).label || row[col.prop] || "-" }}
            </el-tag>
            <span v-else-if="col.money">¥ {{ Number(row[col.prop] || 0).toFixed(2) }}</span>
            <span v-else>{{ row[col.prop] ?? "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="tab === 'fee' || tab === 'urge'" label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button v-if="tab === 'fee'" link type="primary" @click="payOne(row)">缴费</el-button>
            <el-button v-if="tab === 'urge'" link type="warning" @click="urgeOne(row)">催缴</el-button>
          </template>
        </el-table-column>
        <el-table-column v-if="tab === 'receipt'" label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="printOne(row)">补打</el-button>
          </template>
        </el-table-column>
        <el-table-column v-if="tab === 'sync'" label="操作" width="120">
          <template #default>
            <el-button link type="primary" @click="ElMessage.success('已同步到道闸')">同步</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="pickOpen" title="选择房屋" width="560px">
      <el-table :data="roomOptions" size="small" highlight-current-row @row-click="pickRoom">
        <el-table-column prop="roomName" label="房屋" />
        <el-table-column prop="roomNum" label="房号" width="90" />
        <el-table-column prop="state" label="状态" width="90">
          <template #default="{ row }">{{ row.state === "2002" ? "已入住" : "未售" }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import http from "@/api/http";
import { getCommunityId } from "@/utils/community";
import { COMPLAINT_STATE, FEE_STATE, PAY_STATE, REPAIR_STATE } from "@/utils/dict";

const route = useRoute();
const roomCode = ref(route.query.roomCode || "1-1-101");
const searchType = ref("room");
const desk = ref(null);
const tab = ref("fee");
const loading = ref(false);
const pickOpen = ref(false);
const roomOptions = ref([]);
const rows = ref([]);

const owner = computed(() => desk.value?.owner || {});
const room = computed(() => desk.value?.room || {});
const ownerRows = computed(() => [
  { label: "业主编号", value: owner.value.ownerId || "-" },
  { label: "业主姓名", value: owner.value.name || "未绑定业主" },
  { label: "联系电话", value: owner.value.link || "-" },
  { label: "身份证", value: owner.value.idCard || "-" },
  { label: "入住日期", value: desk.value?.inDate || "-" },
  { label: "性别", value: owner.value.sex === "1" ? "女" : owner.value.sex === "0" ? "男" : "-" },
  { label: "业主备注", value: owner.value.remark || "-" },
  { label: "房屋号", value: desk.value?.roomName || "-" },
  { label: "房屋面积", value: `${room.value.builtUpArea || 0} ㎡` },
  { label: "房屋类型", value: room.value.roomSubType === "119" ? "商铺" : "住宅" },
  { label: "户型", value: room.value.apartment || "-" },
  { label: "房屋状态", value: room.value.state === "2002" ? "已入住" : "未售" },
  { label: "室内面积", value: `${room.value.roomArea || 0} ㎡` },
  { label: "房屋备注", value: room.value.remark || "-" },
  { label: "账户余额", value: `¥ ${Number(desk.value?.balance || 0).toFixed(2)}`, money: true }
]);

const columns = computed(() => {
  const maps = {
    fee: [
      { prop: "feeName", label: "费用项目" },
      { prop: "feeId", label: "费用标识", width: 150 },
      { prop: "amount", label: "应收金额", money: true },
      { prop: "startTime", label: "计费开始", width: 160 },
      { prop: "endTime", label: "计费结束", width: 160 },
      { prop: "state", label: "状态", map: FEE_STATE }
    ],
    parkFee: [
      { prop: "feeName", label: "费用项目" },
      { prop: "payerObjId", label: "车位" },
      { prop: "amount", label: "应收金额", money: true },
      { prop: "state", label: "状态", map: FEE_STATE }
    ],
    history: [
      { prop: "detailId", label: "缴费ID", width: 150 },
      { prop: "receivedAmount", label: "实收", money: true },
      { prop: "payTime", label: "缴费时间", width: 170 },
      { prop: "state", label: "状态", map: PAY_STATE }
    ],
    ended: [
      { prop: "feeName", label: "费用项目" },
      { prop: "amount", label: "金额", money: true },
      { prop: "state", label: "状态", map: FEE_STATE }
    ],
    room: [
      { prop: "roomNum", label: "房号" },
      { prop: "layer", label: "楼层" },
      { prop: "apartment", label: "户型" },
      { prop: "builtUpArea", label: "建筑面积" }
    ],
    shop: [
      { prop: "roomNum", label: "铺号" },
      { prop: "apartment", label: "业态" },
      { prop: "builtUpArea", label: "面积" }
    ],
    member: [
      { prop: "name", label: "姓名" },
      { prop: "link", label: "电话" },
      { prop: "idCard", label: "证件号" }
    ],
    car: [
      { prop: "carNum", label: "车牌" },
      { prop: "carBrand", label: "品牌" },
      { prop: "carColor", label: "颜色" },
      { prop: "psId", label: "车位" }
    ],
    urge: [
      { prop: "feeName", label: "费用" },
      { prop: "amount", label: "欠费", money: true },
      { prop: "state", label: "状态", map: FEE_STATE }
    ],
    feedback: [
      { prop: "complaintName", label: "反馈人" },
      { prop: "context", label: "内容", width: 220 },
      { prop: "state", label: "状态", map: COMPLAINT_STATE }
    ],
    repair: [
      { prop: "repairName", label: "报修人" },
      { prop: "context", label: "内容", width: 200 },
      { prop: "state", label: "状态", map: REPAIR_STATE }
    ],
    complaint: [
      { prop: "complaintName", label: "投诉人" },
      { prop: "context", label: "内容", width: 200 },
      { prop: "state", label: "状态", map: COMPLAINT_STATE }
    ],
    sync: [
      { prop: "carNum", label: "车牌" },
      { prop: "carBrand", label: "品牌" },
      { prop: "psId", label: "车位" }
    ],
    receipt: [
      { prop: "detailId", label: "收据号", width: 160 },
      { prop: "receivedAmount", label: "金额", money: true },
      { prop: "payTime", label: "时间", width: 170 }
    ],
    oldOwner: [
      { prop: "name", label: "姓名" },
      { prop: "link", label: "电话" },
      { prop: "startTime", label: "入住" },
      { prop: "endTime", label: "迁出" }
    ],
    meter: [
      { prop: "meterType", label: "表类型" },
      { prop: "preDegrees", label: "上期" },
      { prop: "curDegrees", label: "本期" },
      { prop: "curReadingTime", label: "抄表时间", width: 170 }
    ]
  };
  return maps[tab.value] || [];
});

const tableData = computed(() => rows.value);

const cid = () => getCommunityId();
const roomId = () => desk.value?.room?.roomId;
const ownerId = () => desk.value?.owner?.ownerId;

const search = async () => {
  const res = await http.get("/room.queryRoomByCode", { params: { communityId: cid(), roomCode: roomCode.value } });
  if (res.code === 0) {
    desk.value = res.data;
    loadTab();
  }
};

const pickRoom = async (row) => {
  pickOpen.value = false;
  roomCode.value = row.roomName;
  const res = await http.get("/owner.queryOwnerByRoom", { params: { roomId: row.roomId } });
  if (res.code === 0) {
    desk.value = res.data;
    loadTab();
  }
};

const loadTab = async () => {
  if (!desk.value) {
    return;
  }
  loading.value = true;
  try {
    const id = roomId();
    const oid = ownerId();
    if (tab.value === "fee" || tab.value === "urge") {
      const res = await http.get("/fee.listFee", { params: { communityId: cid(), payerObjId: id, state: "2008001", page: 1, row: 50 } });
      rows.value = res.data || [];
    } else if (tab.value === "ended") {
      const res = await http.get("/fee.listFee", { params: { communityId: cid(), payerObjId: id, state: "2009001", page: 1, row: 50 } });
      rows.value = res.data || [];
    } else if (tab.value === "parkFee") {
      const res = await http.get("/fee.listFee", { params: { communityId: cid(), page: 1, row: 50 } });
      rows.value = (res.data || []).filter((f) => f.payerObjType === "6666");
    } else if (tab.value === "history" || tab.value === "receipt") {
      const fees = await http.get("/fee.listFee", { params: { communityId: cid(), payerObjId: id, page: 1, row: 50 } });
      const ids = new Set((fees.data || []).map((f) => f.feeId));
      const details = await http.get("/fee.queryFeeDetail", { params: { communityId: cid(), page: 1, row: 50 } });
      rows.value = (details.data || []).filter((d) => ids.has(d.feeId));
    } else if (tab.value === "room") {
      rows.value = [desk.value.room];
    } else if (tab.value === "shop") {
      const res = await http.get("/room.queryRooms", { params: { communityId: cid(), roomSubType: "119", page: 1, row: 50 } });
      rows.value = res.data || [];
    } else if (tab.value === "member" && oid) {
      const res = await http.get("/owner.queryOwnerMembers", { params: { ownerId: oid, page: 1, row: 50 } });
      rows.value = res.data || [];
    } else if ((tab.value === "car" || tab.value === "sync") && oid) {
      const res = await http.get("/owner.listOwnerCars", { params: { communityId: cid(), ownerId: oid, page: 1, row: 50 } });
      rows.value = res.data || [];
    } else if (tab.value === "feedback" || tab.value === "complaint") {
      const res = await http.get("/complaint.listComplaints", { params: { communityId: cid(), page: 1, row: 50 } });
      rows.value = res.data || [];
    } else if (tab.value === "repair") {
      const res = await http.get("/repair.listRepairs", { params: { communityId: cid(), page: 1, row: 50 } });
      rows.value = (res.data || []).filter((r) => !r.roomId || r.roomId === id);
    } else if (tab.value === "oldOwner") {
      rows.value = desk.value.historyOwners || [];
    } else if (tab.value === "meter") {
      const res = await http.get("/meterWater.listMeterWaters", { params: { communityId: cid(), objId: id, page: 1, row: 50 } });
      rows.value = res.data || [];
    } else {
      rows.value = [];
    }
  } finally {
    loading.value = false;
  }
};

const payOne = async (row) => {
  const res = await http.post("/fee.payFee", { feeId: row.feeId, receivedAmount: row.amount, cycles: 1 });
  if (res.code === 0) {
    ElMessage.success("缴费成功，待审核");
    loadTab();
  }
};

const urgeOne = async (row) => {
  const res = await http.post("/fee.urgeFee", { feeId: row.feeId });
  if (res.code === 0) {
    ElMessage.success("催缴通知已发送");
  }
};

const batchPay = async () => {
  await ElMessageBox.confirm("确认对该房屋下收费中账单批量缴费？");
  const res = await http.post("/fee.batchPayFee", { payerObjId: roomId() });
  if (res.code === 0) {
    ElMessage.success(`已提交 ${res.data || 0} 笔缴费`);
    loadTab();
  }
};

const prestore = async () => {
  const acctId = desk.value?.account?.acctId;
  if (!acctId) {
    ElMessage.warning("该业主还没有账户");
    return;
  }
  const { value } = await ElMessageBox.prompt("请输入预存金额", "预存收款");
  const res = await http.post("/account.ownerPrestoreAccount", { acctId, amount: value, remark: "业务受理预存" });
  if (res.code === 0) {
    ElMessage.success("预存成功");
    search();
  }
};

const printOne = (row) => {
  const html = `<h2 style="text-align:center">物业收费收据</h2><p>收据号：${row.detailId}</p><p>金额：¥ ${Number(row.receivedAmount || 0).toFixed(2)}</p><p>时间：${row.payTime || ""}</p>`;
  const win = window.open("", "_blank", "width=640,height=480");
  win.document.write(html);
  win.document.close();
  win.print();
};

watch(tab, loadTab);
watch(
  () => route.query.roomCode,
  (code) => {
    if (code) {
      roomCode.value = code;
      search();
    }
  }
);

onMounted(async () => {
  const res = await http.get("/room.listRoomOptions", { params: { communityId: cid() } });
  roomOptions.value = res.data || [];
  search();
});
</script>
