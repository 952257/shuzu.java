<template>
  <crud-page
    title="问卷投票"
    desc="发起小区问卷或业主投票，结束后可归档。"
    list-api="/vote.listVotes"
    save-api="/vote.saveVote"
    update-api="/vote.updateVote"
    delete-api="/vote.deleteVote"
    delete-key="voteId"
    add-text="发起问卷"
    edit-text="编辑问卷"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'title', label: '标题' }]"
    :columns="[
      { prop: 'title', label: '标题', width: 180 },
      { prop: 'voteType', label: '类型', type: 'tag', map: VOTE_TYPE },
      { prop: 'context', label: '内容', width: 220 },
      { prop: 'startTime', label: '开始时间', width: 170 },
      { prop: 'endTime', label: '结束时间', width: 170 },
      { prop: 'state', label: '状态', type: 'tag', map: VOTE_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'title', label: '标题' },
      { prop: 'voteType', label: '类型', type: 'select', options: [{ label: '问卷', value: '1001' }, { label: '投票', value: '1002' }] },
      { prop: 'context', label: '内容', type: 'textarea' },
      { prop: 'startTime', label: '开始时间', type: 'date' },
      { prop: 'endTime', label: '结束时间', type: 'date' }
    ]"
    :default-form="{ communityId: cid, title: '', voteType: '1002', context: '', state: '2000' }"
    :extra-actions="[
      { label: '结束', type: 'warning', api: '/vote.finishVote', body: (row) => ({ voteId: row.voteId }), confirm: '确认结束该问卷？', msg: '已结束' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { VOTE_STATE, VOTE_TYPE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
