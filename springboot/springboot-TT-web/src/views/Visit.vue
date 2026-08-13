<template>
  <crud-page
    title="访客登记"
    desc="登记来访人员、车牌和拜访房屋，到访后确认进入，离开时签离。"
    list-api="/visit.listVisits"
    save-api="/visit.saveVisit"
    update-api="/visit.updateVisit"
    delete-api="/visit.deleteVisit"
    delete-key="visitId"
    add-text="登记访客"
    edit-text="编辑访客"
    :op-width="260"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'name', label: '访客姓名' }]"
    :columns="[
      { prop: 'name', label: '访客' },
      { prop: 'phone', label: '电话' },
      { prop: 'carNum', label: '车牌' },
      { prop: 'ownerName', label: '被访业主' },
      { prop: 'roomName', label: '房屋' },
      { prop: 'reason', label: '事由' },
      { prop: 'state', label: '状态', type: 'tag', map: VISIT_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'name', label: '访客姓名' },
      { prop: 'phone', label: '电话' },
      { prop: 'carNum', label: '车牌' },
      { prop: 'ownerName', label: '被访业主' },
      { prop: 'roomName', label: '房屋' },
      { prop: 'reason', label: '事由' }
    ]"
    :default-form="{ communityId: cid, name: '', phone: '', carNum: '', ownerName: '李明', roomName: '1-1-101', reason: '探访' }"
    :extra-actions="[
      { label: '到访', type: 'success', api: '/visit.arriveVisit', body: (row) => ({ visitId: row.visitId }), msg: '已确认到访' },
      { label: '离开', api: '/visit.leaveVisit', body: (row) => ({ visitId: row.visitId }), msg: '已签离' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { VISIT_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
