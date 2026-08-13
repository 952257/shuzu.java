<template>
  <crud-page
    title="员工管理"
    desc="维护物业公司员工账号，仅管理员和员工角色可登录后台。"
    list-api="/query.staff.infos"
    save-api="/user.staff.add"
    update-api="/user.staff.modify"
    delete-api="/user.staff.delete"
    delete-key="userId"
    add-text="新增员工"
    edit-text="编辑员工"
    :query-fields="[{ prop: 'name', label: '姓名' }, { prop: 'tel', label: '电话' }]"
    :columns="[
      { prop: 'name', label: '姓名' },
      { prop: 'username', label: '用户名' },
      { prop: 'tel', label: '电话' },
      { prop: 'role', label: '角色', type: 'tag', map: ROLE }
    ]"
    :form-fields="[
      { prop: 'name', label: '姓名' },
      { prop: 'username', label: '用户名' },
      { prop: 'tel', label: '电话' },
      { prop: 'password', label: '密码', placeholder: '编辑时不改请留空' },
      { prop: 'role', label: '角色', type: 'select', options: [{ label: '管理员', value: 'ADMIN' }, { label: '员工', value: 'STAFF' }] }
    ]"
    :default-form="{ name: '', username: '', tel: '', password: 'admin', role: 'STAFF' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { ROLE } from "@/utils/dict";
</script>
