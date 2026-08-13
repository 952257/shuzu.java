<template>
  <crud-page
    title="操作日志"
    desc="员工登录后台的记录，便于审计。"
    list-api="/user.listLoginLogs"
    :columns="[
      { prop: 'userName', label: '员工' },
      { prop: 'userId', label: '用户ID', width: 160 },
      { prop: 'source', label: '来源' },
      { prop: 'loginTime', label: '登录时间', width: 180 }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
</script>
