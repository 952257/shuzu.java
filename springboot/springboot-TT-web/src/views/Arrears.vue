<template>
  <crud-page
    title="欠费催缴"
    desc="查看仍在收费中的账单，向业主发送催缴通知。"
    list-api="/fee.listFee"
    :op-width="160"
    :default-query="{ communityId: cid, state: '2008001' }"
    :query-fields="[{ prop: 'payerObjId', label: '房屋/车位ID' }]"
    :columns="[
      { prop: 'feeName', label: '费用' },
      { prop: 'payerObjId', label: '缴费对象', width: 160 },
      { prop: 'amount', label: '欠费金额', type: 'money' },
      { prop: 'endTime', label: '计费结束', width: 170 },
      { prop: 'state', label: '状态', type: 'tag', map: FEE_STATE }
    ]"
    :extra-actions="[
      { label: '催缴', type: 'warning', api: '/fee.urgeFee', body: (row) => ({ feeId: row.feeId }), confirm: '确认向该业主发送催缴通知？', msg: '催缴通知已发送' },
      { label: '缴费', type: 'success', api: '/fee.payFee', body: (row) => ({ feeId: row.feeId, receivedAmount: row.amount, cycles: 1 }), msg: '缴费成功，待审核' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { FEE_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
