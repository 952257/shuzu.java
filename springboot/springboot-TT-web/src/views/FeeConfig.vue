<template>
  <crud-page
    title="费用项"
    kicker="收费配置"
    desc="费用项决定如何计费。创建账单前请先配置物业费、水费或电费。"
    list-api="/feeConfig.listFeeConfigs"
    save-api="/feeConfig.saveFeeConfig"
    update-api="/feeConfig.updateFeeConfig"
    delete-api="/feeConfig.deleteFeeConfig"
    delete-key="configId"
    add-text="添加费用项"
    edit-text="编辑费用项"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'feeName', label: '费用项' }]"
    :columns="[
      { prop: 'feeName', label: '名称' },
      { prop: 'feeTypeCd', label: '类型编码', width: 140 },
      { prop: 'squarePrice', label: '单价', type: 'money' },
      { prop: 'paymentCycle', label: '周期(月)' }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'feeName', label: '名称' },
      { prop: 'feeTypeCd', label: '类型编码' },
      { prop: 'squarePrice', label: '单价', type: 'number', step: 0.1 },
      { prop: 'paymentCycle', label: '周期' }
    ]"
    :default-form="{ communityId: cid, feeTypeCd: '888800010001', feeName: '物业费', feeFlag: '1003006', squarePrice: 1.5, additionalAmount: 0, paymentCycle: '12' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
