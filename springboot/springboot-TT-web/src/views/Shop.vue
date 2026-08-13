<template>
  <crud-page
    title="商铺管理"
    desc="商铺按房屋档案管理，类型为商铺。交房后可绑定商户业主。"
    list-api="/room.queryRooms"
    save-api="/room.saveRoom"
    update-api="/room.updateRoom"
    delete-api="/room.deleteRoom"
    delete-key="roomId"
    add-text="添加商铺"
    edit-text="修改商铺"
    :op-width="280"
    :default-query="{ communityId: cid, roomSubType: '119' }"
    :query-fields="[{ prop: 'roomNum', label: '商铺编号' }]"
    :columns="[
      { prop: 'roomNum', label: '铺号' },
      { prop: 'layer', label: '楼层' },
      { prop: 'apartment', label: '业态' },
      { prop: 'builtUpArea', label: '建筑面积㎡' },
      { prop: 'roomRent', label: '租金', type: 'money' },
      { prop: 'state', label: '状态', type: 'tag', map: ROOM_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'unitId', label: '单元ID' },
      { prop: 'roomNum', label: '铺号' },
      { prop: 'layer', label: '楼层' },
      { prop: 'apartment', label: '业态' },
      { prop: 'builtUpArea', label: '建筑面积', type: 'number' },
      { prop: 'roomRent', label: '租金', type: 'number', step: 0.01 }
    ]"
    :default-form="{ communityId: cid, unitId: '4022081500000001', roomNum: '', layer: '1', apartment: '商铺', builtUpArea: 45, roomArea: 40, roomRent: 0, state: '2001', roomSubType: '119' }"
    :extra-actions="[
      { label: '交铺', type: 'success', api: '/room.sellRoom', body: (row) => ({ roomId: row.roomId }), prompt: { key: 'ownerId', title: '交铺', message: '请输入业主ID' }, msg: '交铺成功' },
      { label: '退铺', type: 'warning', api: '/room.exitRoom', body: (row) => ({ roomId: row.roomId }), prompt: { key: 'ownerId', title: '退铺', message: '请输入业主ID' }, msg: '已退铺' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { ROOM_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
