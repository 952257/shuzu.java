<template>
  <crud-page
    title="水电抄表"
    desc="录入上期和本期读数，用于生成水费、电费。"
    list-api="/meterWater.listMeterWaters"
    save-api="/meterWater.saveMeterWater"
    update-api="/meterWater.updateMeterWater"
    delete-api="/meterWater.deleteMeterWater"
    delete-key="waterId"
    add-text="抄表"
    edit-text="编辑抄表"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'meterType', label: '表类型', type: 'select', options: [{ label: '水表', value: '2020' }, { label: '电表', value: '3030' }] }]"
    :columns="[
      { prop: 'objId', label: '房屋ID', width: 160 },
      { prop: 'meterType', label: '类型', type: 'tag', map: METER_TYPE },
      { prop: 'preDegrees', label: '上期读数' },
      { prop: 'curDegrees', label: '本期读数' }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'objId', label: '房屋ID' },
      { prop: 'meterType', label: '类型', type: 'select', options: [{ label: '水表', value: '2020' }, { label: '电表', value: '3030' }] },
      { prop: 'preDegrees', label: '上期', type: 'number' },
      { prop: 'curDegrees', label: '本期', type: 'number' }
    ]"
    :default-form="{ communityId: cid, objId: '', meterType: '2020', preDegrees: 0, curDegrees: 10 }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { METER_TYPE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
