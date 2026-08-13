<template>
  <crud-page
    title="投诉建议"
    desc="登记业主投诉或建议，处理后状态会变为已完成。"
    list-api="/complaint.listComplaints"
    save-api="/complaint.saveComplaint"
    update-api="/complaint.updateComplaint"
    delete-api="/complaint.deleteComplaint"
    delete-key="complaintId"
    add-text="登记投诉"
    edit-text="编辑投诉"
    :default-query="{ communityId: '2022081539020475' }"
    :query-fields="[{ prop: 'typeCd', label: '类型', type: 'select', options: [{ label: '投诉', value: '809001' }, { label: '建议', value: '809002' }] }]"
    :columns="[
      { prop: 'complaintName', label: '投诉人' },
      { prop: 'tel', label: '电话' },
      { prop: 'typeCd', label: '类型', type: 'tag', map: COMPLAINT_TYPE },
      { prop: 'context', label: '内容', width: 200 },
      { prop: 'state', label: '状态', type: 'tag', map: COMPLAINT_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'complaintName', label: '姓名' },
      { prop: 'tel', label: '电话' },
      { prop: 'typeCd', label: '类型', type: 'select', options: [{ label: '投诉', value: '809001' }, { label: '建议', value: '809002' }] },
      { prop: 'context', label: '内容', type: 'textarea' }
    ]"
    :default-form="{ communityId: '2022081539020475', typeCd: '809001', complaintName: '', tel: '', context: '' }"
    :extra-actions="[
      { label: '处理', type: 'success', api: '/complaint.auditComplaint', body: (row) => ({ complaintId: row.complaintId }), msg: '已处理' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { COMPLAINT_STATE, COMPLAINT_TYPE } from "@/utils/dict";
</script>
