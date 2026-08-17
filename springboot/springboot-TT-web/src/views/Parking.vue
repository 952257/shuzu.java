<template>
  <crud-page
    title="车位管理"
    desc="空闲车位可出售或出租，退还后重新变为空闲。"
    list-api="/parkingSpace.queryParkingSpaces"
    save-api="/parkingSpace.saveParkingSpace"
    update-api="/parkingSpace.editParkingSpace"
    delete-api="/parkingSpace.deleteParkingSpace"
    delete-key="psId"
    add-text="添加车位"
    edit-text="编辑车位"
    :op-width="280"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'num', label: '车位号' }]"
    :columns="[
      { prop: 'num', label: '车位号' },
      { prop: 'parkingType', label: '类型', type: 'tag', map: PARKING_TYPE },
      { prop: 'area', label: '面积㎡' },
      { prop: 'state', label: '状态', type: 'tag', map: PARKING_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'num', label: '车位号' },
      { prop: 'parkingType', label: '类型', type: 'select', options: [{ label: '地上', value: '1' }, { label: '地下', value: '2' }] },
      { prop: 'area', label: '面积', type: 'number' }
    ]"
    :default-form="{ communityId: cid, num: '', parkingType: '1', state: 'F', area: 12 }"
    :extra-actions="[
      { label: '出售', type: 'success', api: '/parkingSpace.sellParkingSpace', body: (row) => ({ psId: row.psId }), msg: '已出售' },
      { label: '退还', type: 'warning', api: '/parkingSpace.exitParkingSpace', body: (row) => ({ psId: row.psId }), msg: '已退还' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { PARKING_STATE, PARKING_TYPE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
