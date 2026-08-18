<template>
  <crud-page
    title="楼栋管理"
    desc="先选小区，再按编号维护楼栋，后续单元会挂在楼栋下。"
    list-api="/floor.queryFloors"
    save-api="/floor.saveFloor"
    update-api="/floor.editFloor"
    delete-api="/floor.deleteFloor"
    delete-key="floorId"
    add-text="添加楼栋"
    edit-text="编辑楼栋"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'floorNum', label: '楼栋编号' }, { prop: 'name', label: '楼栋名称' }]"
    :columns="[
      { prop: 'floorId', label: '楼栋ID', width: 160 },
      { prop: 'floorNum', label: '编号' },
      { prop: 'name', label: '名称' },
      { prop: 'floorArea', label: '面积㎡' },
      { prop: 'seq', label: '排序' }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'floorNum', label: '编号' },
      { prop: 'name', label: '名称' },
      { prop: 'floorArea', label: '面积', type: 'number' }
    ]"
    :default-form="{ communityId: cid, floorNum: '', name: '', floorArea: 0, seq: 1 }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
