<template>
  <crud-page
    title="费用账单"
    desc="按房屋或车位创建应收费用。缴费后进入审核，通过后计入实收。"
    list-api="/fee.listFee"
    save-api="/fee.saveFee"
    delete-api="/fee.deleteFee"
    delete-key="feeId"
    delete-text="结束费用"
    add-text="创建费用"
    :op-width="200"
    :default-query="{ communityId: '2022081539020475' }"
    :query-fields="[{ prop: 'payerObjId', label: '房屋/车位ID' }]"
    :columns="[
      { prop: 'feeName', label: '费用' },
      { prop: 'payerObjId', label: '缴费对象', width: 160 },
      { prop: 'amount', label: '金额', type: 'money' },
      { prop: 'state', label: '状态', type: 'tag', map: FEE_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'configId', label: '费用项ID' },
      { prop: 'payerObjId', label: '房屋/车位ID' },
      { prop: 'amount', label: '金额', type: 'number', step: 0.01 }
    ]"
    :default-form="{ communityId: '2022081539020475', configId: 'A022081500000001', payerObjId: '5022081500000001', payerObjType: '3333', amount: 134.25 }"
    :extra-actions="[
      { label: '缴费', type: 'success', api: '/fee.payFee', body: (row) => ({ feeId: row.feeId, receivedAmount: row.amount, cycles: 1 }), msg: '缴费成功，待财务审核' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { FEE_STATE } from "@/utils/dict";
</script>
