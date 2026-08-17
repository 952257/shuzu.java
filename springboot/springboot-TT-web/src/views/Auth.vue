<template>
  <crud-page
    title="房屋认证"
    desc="业主提交房屋绑定申请后，在此审核通过或拒绝。"
    list-api="/owner.listAppUserBindingOwners"
    save-api="/owner.saveOwnerAppUser"
    delete-key="appUserId"
    add-text="提交认证"
    :op-width="200"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'state', label: '状态', type: 'select', options: [{ label: '待审核', value: '10000' }, { label: '通过', value: '12000' }, { label: '拒绝', value: '13000' }] }]"
    :columns="[
      { prop: 'appUserName', label: '姓名' },
      { prop: 'link', label: '手机' },
      { prop: 'idCard', label: '证件号', width: 180 },
      { prop: 'roomName', label: '房屋' },
      { prop: 'state', label: '状态', type: 'tag', map: AUTH_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'appUserName', label: '姓名' },
      { prop: 'link', label: '手机' },
      { prop: 'idCard', label: '证件号' },
      { prop: 'roomName', label: '房屋' },
      { prop: 'memberId', label: '业主ID' }
    ]"
    :default-form="{ communityId: cid, appUserName: '', link: '', idCard: '', roomName: '', memberId: '' }"
    :extra-actions="[
      { label: '通过', type: 'success', api: '/owner.auditAuthOwner', body: (row) => ({ appUserId: row.appUserId, state: '12000' }), msg: '已通过' },
      { label: '拒绝', type: 'danger', api: '/owner.auditAuthOwner', body: (row) => ({ appUserId: row.appUserId, state: '13000', remark: '资料不符' }), msg: '已拒绝' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { AUTH_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
