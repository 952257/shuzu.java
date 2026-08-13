<template>
  <crud-page
    title="单元管理"
    desc="单元属于某一栋楼，请先填写正确的楼栋ID。"
    list-api="/unit.queryUnits"
    save-api="/unit.saveUnit"
    update-api="/unit.updateUnit"
    delete-api="/unit.deleteUnit"
    delete-key="unitId"
    add-text="添加单元"
    edit-text="编辑单元"
    :default-query="{ floorId: '3022081500000001' }"
    :query-fields="[{ prop: 'floorId', label: '楼栋ID' }]"
    :columns="[
      { prop: 'unitId', label: '单元ID', width: 160 },
      { prop: 'unitNum', label: '单元号' },
      { prop: 'layerCount', label: '层数' },
      { prop: 'lift', label: '电梯', type: 'tag', map: LIFT },
      { prop: 'unitArea', label: '面积㎡' }
    ]"
    :form-fields="[
      { prop: 'floorId', label: '楼栋ID' },
      { prop: 'unitNum', label: '单元号' },
      { prop: 'layerCount', label: '层数', type: 'number' },
      { prop: 'lift', label: '电梯', type: 'select', options: [{ label: '有电梯', value: '1010' }, { label: '无电梯', value: '2020' }] }
    ]"
    :default-form="{ floorId: '3022081500000001', unitNum: '', layerCount: 6, lift: '1010', unitArea: 0 }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { LIFT } from "@/utils/dict";
</script>
