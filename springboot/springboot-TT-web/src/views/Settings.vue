<template>
  <div class="settings-page">
    <aside class="settings-nav glass-lite">
      <button v-for="g in groups" :key="g.id" :class="{ active: group === g.id }" @click="group = g.id">{{ g.title }}</button>
    </aside>
    <div class="settings-form glass-lite">
      <h3>{{ current.title }}</h3>
      <el-form label-width="140px" v-if="list.length">
        <el-form-item v-for="item in list" :key="item.settingKey" :label="item.settingName">
          <el-input v-model="item.settingValue" :placeholder="'必填，请输入' + item.settingName" />
          <div class="muted">{{ item.remark }}</div>
        </el-form-item>
        <p class="warn">温馨提示：填写前后不要有空格</p>
        <el-form-item>
          <el-button type="primary" @click="save">提交</el-button>
        </el-form-item>
      </el-form>
      <el-empty v-else description="暂无配置" />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import http from "@/api/http";
import { getCommunityId } from "@/utils/community";

const groups = [
  { id: "fee", title: "费用" },
  { id: "pay", title: "支付宝支付" },
  { id: "msg", title: "消息推送" },
  { id: "discount", title: "折扣比例" },
  { id: "points", title: "最大使用积分数" },
  { id: "purchase", title: "紧急采购次数" },
  { id: "repair", title: "维修工单" },
  { id: "refund", title: "退费收据开关" },
  { id: "sms", title: "阿里短信" }
];

const group = ref("fee");
const all = ref([]);
const current = computed(() => groups.find((g) => g.id === group.value) || groups[0]);
const list = computed(() => all.value.filter((i) => i.settingGroup === group.value));

const load = async () => {
  const res = await http.get("/communitySetting.listSettings", { params: { communityId: getCommunityId() } });
  all.value = res.data || [];
};

const save = async () => {
  const res = await http.post("/communitySetting.saveSettings", {
    communityId: getCommunityId(),
    settings: list.value.map((i) => ({
      settingKey: i.settingKey,
      settingName: i.settingName,
      settingValue: String(i.settingValue || "").trim(),
      settingGroup: i.settingGroup,
      remark: i.remark
    }))
  });
  if (res.code === 0) {
    ElMessage.success("已保存");
    load();
  }
};

watch(group, () => {});
onMounted(load);
</script>
