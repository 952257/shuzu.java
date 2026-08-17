<template>
  <crud-page
    title="业主信息"
    desc="业主是房屋和车位的使用人。交房前请先在这里建档。"
    list-api="/owner.queryOwners"
    save-api="/owner.saveOwner"
    update-api="/owner.editOwner"
    delete-api="/owner.deleteOwner"
    delete-key="memberId"
    add-text="添加业主"
    edit-text="编辑业主"
    :default-query="{ communityId: cid, ownerTypeCd: '1001' }"
    :query-fields="[{ prop: 'name', label: '业主姓名' }, { prop: 'link', label: '手机号' }]"
    :columns="[
      { prop: 'name', label: '姓名' },
      { prop: 'sex', label: '性别', type: 'tag', map: SEX },
      { prop: 'link', label: '手机' },
      { prop: 'idCard', label: '证件号', width: 180 },
      { prop: 'address', label: '住址', width: 160 },
      { prop: 'personRole', label: '角色', type: 'tag', map: PERSON_ROLE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'name', label: '姓名' },
      { prop: 'sex', label: '性别', type: 'select', options: [{ label: '男', value: '0' }, { label: '女', value: '1' }] },
      { prop: 'link', label: '手机' },
      { prop: 'idCard', label: '证件号' },
      { prop: 'address', label: '住址' }
    ]"
    :default-form="{ communityId: cid, name: '', link: '', idCard: '', sex: '0', ownerTypeCd: '1001', personRole: '1', address: '' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { PERSON_ROLE, SEX } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
