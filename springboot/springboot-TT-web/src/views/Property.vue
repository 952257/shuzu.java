<template>
  <crud-page
    title="物业公司"
    desc="一个物业公司可以管理多个小区，先建公司再维护小区。"
    list-api="/property.listProperty"
    save-api="/property.saveProperty"
    update-api="/property.updateProperty"
    delete-api="/property.deleteProperty"
    delete-key="storeId"
    add-text="添加物业"
    edit-text="修改物业"
    :query-fields="[{ prop: 'name', label: '物业名称' }, { prop: 'tel', label: '电话' }]"
    :columns="[
      { prop: 'storeId', label: '编号' },
      { prop: 'name', label: '名称' },
      { prop: 'tel', label: '电话' },
      { prop: 'address', label: '地址', width: 180 },
      { prop: 'corporation', label: '法人' },
      { prop: 'foundingTime', label: '成立日期' }
    ]"
    :form-fields="[
      { prop: 'name', label: '名称' },
      { prop: 'tel', label: '电话' },
      { prop: 'address', label: '地址' },
      { prop: 'nearbyLandmarks', label: '地标' },
      { prop: 'corporation', label: '法人' },
      { prop: 'foundingTime', label: '成立日期', placeholder: '2010-01-01' }
    ]"
    :default-form="{ name: '', tel: '', address: '', nearbyLandmarks: '', corporation: '', foundingTime: '' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
</script>
