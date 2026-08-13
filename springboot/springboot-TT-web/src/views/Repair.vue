<template>
  <crud-page
    title="报修工单"
    desc="业主报修后先派单给维修人员，处理完成后标记完成。"
    list-api="/repair.listRepairs"
    save-api="/repair.saveRepair"
    update-api="/repair.updateRepair"
    delete-api="/repair.deleteRepair"
    delete-key="repairId"
    add-text="登记报修"
    edit-text="编辑报修"
    :op-width="240"
    :default-query="{ communityId: '2022081539020475' }"
    :query-fields="[{ prop: 'repairName', label: '报修人' }]"
    :columns="[
      { prop: 'repairName', label: '报修人' },
      { prop: 'tel', label: '电话' },
      { prop: 'repairObjName', label: '位置' },
      { prop: 'context', label: '内容', width: 180 },
      { prop: 'staffName', label: '处理人' },
      { prop: 'state', label: '状态', type: 'tag', map: REPAIR_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'repairName', label: '报修人' },
      { prop: 'tel', label: '电话' },
      { prop: 'repairObjName', label: '位置' },
      { prop: 'context', label: '内容', type: 'textarea' }
    ]"
    :default-form="{ communityId: '2022081539020475', repairName: '', tel: '', context: '', repairObjName: '1-1-101' }"
    :extra-actions="[
      { label: '派单', api: '/repair.dispatchRepair', body: (row) => ({ repairId: row.repairId, staffId: '1000000002', staffName: '吴学文' }), msg: '已派单' },
      { label: '完成', type: 'success', api: '/repair.finishRepair', body: (row) => ({ repairId: row.repairId }), msg: '已完成' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { REPAIR_STATE } from "@/utils/dict";
</script>
