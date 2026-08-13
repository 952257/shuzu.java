<template>
  <crud-page
    title="公告通知"
    desc="向小区业主发布停水停电、环境整治等通知公告。"
    list-api="/notice.listNotices"
    save-api="/notice.saveNotice"
    update-api="/notice.updateNotice"
    delete-api="/notice.deleteNotice"
    delete-key="noticeId"
    add-text="发布公告"
    edit-text="编辑公告"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'title', label: '标题' }]"
    :columns="[
      { prop: 'title', label: '标题', width: 180 },
      { prop: 'noticeType', label: '类型', type: 'tag', map: NOTICE_TYPE },
      { prop: 'context', label: '内容', width: 220 },
      { prop: 'startTime', label: '开始时间', width: 170 },
      { prop: 'endTime', label: '结束时间', width: 170 },
      { prop: 'state', label: '状态', type: 'tag', map: NOTICE_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'title', label: '标题' },
      { prop: 'noticeType', label: '类型', type: 'select', options: [{ label: '通知', value: '1001' }, { label: '公告', value: '1002' }] },
      { prop: 'context', label: '内容', type: 'textarea' },
      { prop: 'startTime', label: '开始时间', type: 'date' },
      { prop: 'endTime', label: '结束时间', type: 'date' }
    ]"
    :default-form="{ communityId: cid, title: '', noticeType: '1002', context: '', state: '2000' }"
    :extra-actions="[
      { label: '发布', type: 'success', api: '/notice.publishNotice', body: (row) => ({ noticeId: row.noticeId }), msg: '已发布' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { NOTICE_STATE, NOTICE_TYPE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
