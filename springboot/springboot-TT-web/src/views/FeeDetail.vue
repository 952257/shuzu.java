<template>
  <crud-page
    title="缴费记录"
    desc="查看每笔实收、应收和缴费时间，已审核通过的记录可退费。"
    list-api="/fee.queryFeeDetail"
    :op-width="140"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'feeId', label: '费用ID' }]"
    :columns="[
      { prop: 'feeId', label: '费用ID', width: 160 },
      { prop: 'receivedAmount', label: '实收', type: 'money' },
      { prop: 'receivableAmount', label: '应收', type: 'money' },
      { prop: 'payTime', label: '缴费时间', width: 170 },
      { prop: 'auditState', label: '审核', type: 'tag', map: AUDIT_STATE },
      { prop: 'state', label: '状态', type: 'tag', map: PAY_STATE }
    ]"
    :extra-actions="[
      { label: '退费', type: 'danger', api: '/fee.refundFee', body: (row) => ({ detailId: row.detailId }), prompt: { key: 'remark', title: '退费', message: '请填写退费原因' }, msg: '已退费' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { AUDIT_STATE, PAY_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
