<template>
  <crud-page
    title="缴费审核"
    desc="收银员收款后由财务审核。通过后计入实收，拒绝后需重新缴费。"
    list-api="/fee.queryFeeDetail"
    :op-width="200"
    :default-query="{ communityId: cid, auditState: '1000' }"
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
      { label: '通过', type: 'success', api: '/fee.auditFee', body: (row) => ({ detailId: row.detailId, auditState: '1100' }), msg: '审核通过' },
      { label: '拒绝', type: 'danger', api: '/fee.auditFee', body: (row) => ({ detailId: row.detailId, auditState: '1200' }), prompt: { key: 'remark', title: '拒绝原因', message: '请填写拒绝原因' }, msg: '已拒绝' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { AUDIT_STATE, PAY_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
