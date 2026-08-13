<template>
  <crud-page
    title="组织架构"
    desc="维护物业公司部门层级，员工可挂到对应组织。"
    list-api="/org.listOrgs"
    save-api="/org.saveOrg"
    update-api="/org.updateOrg"
    delete-api="/org.deleteOrg"
    delete-key="orgId"
    add-text="新增部门"
    edit-text="编辑部门"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'orgName', label: '部门名称' }]"
    :columns="[
      { prop: 'orgName', label: '部门' },
      { prop: 'orgLevel', label: '层级' },
      { prop: 'parentId', label: '上级ID', width: 160 },
      { prop: 'description', label: '说明', width: 180 }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'orgName', label: '部门名称' },
      { prop: 'orgLevel', label: '层级' },
      { prop: 'parentId', label: '上级部门ID' },
      { prop: 'description', label: '说明' }
    ]"
    :default-form="{ communityId: cid, orgName: '', orgLevel: '2', parentId: 'ORG0000000000001', description: '' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
