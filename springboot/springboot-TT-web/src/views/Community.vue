<template>
  <crud-page
    title="小区管理"
    desc="小区是后续楼栋、房屋、业主和费用的归属单位。"
    list-api="/community.listCommunitys"
    save-api="/community.saveCommunity"
    update-api="/community.updateCommunity"
    delete-api="/community.deleteCommunity"
    delete-key="communityId"
    add-text="添加小区"
    edit-text="修改小区"
    :query-fields="[{ prop: 'name', label: '小区名称' }, { prop: 'cityCode', label: '地区编码' }]"
    :columns="[
      { prop: 'communityId', label: '小区ID', width: 160 },
      { prop: 'name', label: '名称' },
      { prop: 'cityName', label: '城市', width: 160 },
      { prop: 'address', label: '地址', width: 180 },
      { prop: 'tel', label: '电话' },
      { prop: 'state', label: '状态', type: 'tag', map: COMMUNITY_STATE }
    ]"
    :form-fields="[
      { prop: 'name', label: '名称' },
      { prop: 'address', label: '地址' },
      { prop: 'cityCode', label: '地区编码' },
      { prop: 'cityName', label: '城市' },
      { prop: 'nearbyLandmarks', label: '地标' },
      { prop: 'tel', label: '电话' },
      { prop: 'payFeeMonth', label: '收费周期', type: 'number' }
    ]"
    :default-form="{ name: '', address: '', cityCode: '630104', cityName: '青海省西宁市城西区', nearbyLandmarks: '国投广场', tel: '', payFeeMonth: 12, feePrice: 0 }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { COMMUNITY_STATE } from "@/utils/dict";
</script>
