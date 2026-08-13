<template>
  <crud-page
    title="房屋管理"
    desc="房屋建好后，通过交房绑定业主；退房后房屋重新变为未售。"
    list-api="/room.queryRooms"
    save-api="/room.saveRoom"
    update-api="/room.updateRoom"
    delete-api="/room.deleteRoom"
    delete-key="roomId"
    add-text="添加房屋"
    edit-text="修改房屋"
    :op-width="280"
    :default-query="{ communityId: '2022081539020475', roomSubType: '110' }"
    :query-fields="[{ prop: 'communityId', label: '小区ID' }, { prop: 'roomNum', label: '房屋编号' }]"
    :columns="[
      { prop: 'roomNum', label: '房号' },
      { prop: 'layer', label: '楼层' },
      { prop: 'apartment', label: '户型' },
      { prop: 'builtUpArea', label: '建筑面积㎡' },
      { prop: 'roomArea', label: '套内面积㎡' },
      { prop: 'roomSubType', label: '类型', type: 'tag', map: ROOM_SUB_TYPE },
      { prop: 'state', label: '状态', type: 'tag', map: ROOM_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'unitId', label: '单元ID' },
      { prop: 'roomNum', label: '房号' },
      { prop: 'layer', label: '楼层' },
      { prop: 'apartment', label: '户型' },
      { prop: 'builtUpArea', label: '建筑面积', type: 'number' }
    ]"
    :default-form="{ communityId: '2022081539020475', unitId: '4022081500000001', roomNum: '', layer: '1', apartment: '两室一厅', builtUpArea: 90, roomArea: 80, state: '2001', roomSubType: '110' }"
    :extra-actions="[
      { label: '交房', type: 'success', api: '/room.sellRoom', body: (row) => ({ roomId: row.roomId }), prompt: { key: 'ownerId', title: '交房', message: '请输入业主ID' }, msg: '交房成功' },
      { label: '退房', type: 'warning', api: '/room.exitRoom', body: (row) => ({ roomId: row.roomId }), prompt: { key: 'ownerId', title: '退房', message: '请输入业主ID' }, msg: '已退房' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { ROOM_STATE, ROOM_SUB_TYPE } from "@/utils/dict";
</script>
