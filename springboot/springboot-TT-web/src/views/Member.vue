<template>
  <crud-page
    title="家庭成员"
    desc="成员挂在业主名下，添加时请填写正确的业主ID。"
    list-api="/owner.queryOwners"
    save-api="/owner.saveOwner"
    update-api="/owner.editOwner"
    delete-api="/owner.deleteOwner"
    delete-key="memberId"
    add-text="添加成员"
    edit-text="编辑成员"
    :default-query="{ communityId: '2022081539020475', ownerTypeCd: '1002' }"
    :query-fields="[{ prop: 'name', label: '成员姓名' }]"
    :columns="[
      { prop: 'name', label: '姓名' },
      { prop: 'link', label: '手机' },
      { prop: 'ownerId', label: '所属业主ID', width: 160 },
      { prop: 'personRole', label: '角色', type: 'tag', map: PERSON_ROLE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'ownerId', label: '业主ID' },
      { prop: 'name', label: '姓名' },
      { prop: 'link', label: '手机' }
    ]"
    :default-form="{ communityId: '2022081539020475', ownerId: '6022081500000001', name: '', link: '', ownerTypeCd: '1002', personRole: '3' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { PERSON_ROLE } from "@/utils/dict";
</script>
