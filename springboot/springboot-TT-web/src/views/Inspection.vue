<template>
  <crud-page
    title="巡检任务"
    desc="安排安防、消防等巡检点，完成后登记结果。"
    list-api="/inspection.listInspections"
    save-api="/inspection.saveInspection"
    update-api="/inspection.updateInspection"
    delete-api="/inspection.deleteInspection"
    delete-key="taskId"
    add-text="新建巡检"
    edit-text="编辑巡检"
    :op-width="240"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'planName', label: '计划名称' }]"
    :columns="[
      { prop: 'planName', label: '计划' },
      { prop: 'pointName', label: '巡检点' },
      { prop: 'staffName', label: '巡检人' },
      { prop: 'inspectTime', label: '巡检时间', width: 170 },
      { prop: 'remark', label: '备注' },
      { prop: 'state', label: '状态', type: 'tag', map: INSPECT_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'planName', label: '计划名称' },
      { prop: 'pointName', label: '巡检点' },
      { prop: 'staffName', label: '巡检人' }
    ]"
    :default-form="{ communityId: cid, planName: '日常安防巡检', pointName: '', staffName: '吴学文', state: '1000' }"
    :extra-actions="[
      { label: '完成', type: 'success', api: '/inspection.finishInspection', body: (row) => ({ taskId: row.taskId, staffName: row.staffName || '吴学文' }), prompt: { key: 'remark', title: '巡检结果', message: '请填写巡检备注' }, msg: '巡检完成' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { INSPECT_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
