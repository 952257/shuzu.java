<template>
  <crud-page
    title="单元管理"
    desc="单元挂在楼栋下。先选当前小区的楼栋，再添加单元号。"
    list-api="/unit.queryUnits"
    save-api="/unit.saveUnit"
    update-api="/unit.updateUnit"
    delete-api="/unit.deleteUnit"
    delete-key="unitId"
    add-text="添加单元"
    edit-text="编辑单元"
    :default-query="{ communityId: cid, floorId: '' }"
    :query-fields="queryFields"
    :columns="[
      { prop: 'unitId', label: '单元ID', width: 160 },
      { prop: 'floorId', label: '楼栋ID', width: 160 },
      { prop: 'unitNum', label: '单元号' },
      { prop: 'layerCount', label: '层数' },
      { prop: 'lift', label: '电梯', type: 'tag', map: LIFT },
      { prop: 'unitArea', label: '面积㎡' }
    ]"
    :form-fields="formFields"
    :default-form="{ floorId: '', unitNum: '', layerCount: 6, lift: '1010', unitArea: 0 }"
  />
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import CrudPage from "@/components/CrudPage.vue";
import http from "@/api/http";
import { LIFT } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";

const cid = getCommunityId();
const floors = ref([]);

const floorOptions = computed(() =>
  floors.value.map((f) => ({
    label: `${f.name || f.floorNum || "楼栋"}`,
    value: f.floorId
  }))
);

const queryFields = computed(() => [
  { prop: "floorId", label: "楼栋", type: "select", options: floorOptions.value }
]);

const formFields = computed(() => [
  { prop: "floorId", label: "楼栋", type: "select", options: floorOptions.value },
  { prop: "unitNum", label: "单元号" },
  { prop: "layerCount", label: "层数", type: "number" },
  { prop: "lift", label: "电梯", type: "select", options: [{ label: "有电梯", value: "1010" }, { label: "无电梯", value: "2020" }] }
]);

onMounted(async () => {
  const res = await http.get("/floor.queryFloors", { params: { communityId: cid, page: 1, row: 100 } });
  if (res.code === 0) {
    floors.value = res.data || [];
  }
});
</script>
