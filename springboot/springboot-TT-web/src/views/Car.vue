<template>
  <crud-page
    title="车辆管理"
    desc="登记业主车辆并关联车位，便于后续停车收费。"
    list-api="/owner.listOwnerCars"
    save-api="/owner.saveOwnerCar"
    update-api="/owner.updateOwnerCar"
    delete-api="/owner.deleteOwnerCar"
    delete-key="carId"
    add-text="添加车辆"
    edit-text="编辑车辆"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'carNum', label: '车牌号' }, { prop: 'ownerId', label: '业主ID' }]"
    :columns="[
      { prop: 'carNum', label: '车牌' },
      { prop: 'carBrand', label: '品牌' },
      { prop: 'carColor', label: '颜色' },
      { prop: 'ownerId', label: '业主ID', width: 160 },
      { prop: 'psId', label: '车位ID', width: 160 }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'ownerId', label: '业主ID' },
      { prop: 'carNum', label: '车牌' },
      { prop: 'carBrand', label: '品牌' },
      { prop: 'carColor', label: '颜色' },
      { prop: 'psId', label: '车位ID' }
    ]"
    :default-form="{ communityId: cid, ownerId: '', carNum: '', carBrand: '', carColor: '', psId: '' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
