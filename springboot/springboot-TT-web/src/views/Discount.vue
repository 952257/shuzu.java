<template>
  <crud-page
    title="缴费折扣"
    desc="配置预缴打折或定额减免，收费时按规则优惠。"
    list-api="/feeDiscount.listDiscounts"
    save-api="/feeDiscount.saveDiscount"
    update-api="/feeDiscount.updateDiscount"
    delete-api="/feeDiscount.deleteDiscount"
    delete-key="discountId"
    add-text="新增折扣"
    edit-text="编辑折扣"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'discountName', label: '折扣名称' }]"
    :columns="[
      { prop: 'discountName', label: '名称' },
      { prop: 'discountType', label: '类型', type: 'tag', map: DISCOUNT_TYPE },
      { prop: 'specValue', label: '折扣值' },
      { prop: 'remark', label: '说明' },
      { prop: 'state', label: '状态', type: 'tag', map: DISCOUNT_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'discountName', label: '折扣名称' },
      { prop: 'discountType', label: '类型', type: 'select', options: [{ label: '打折', value: '1001' }, { label: '减免', value: '2002' }] },
      { prop: 'specValue', label: '折扣值', type: 'number', step: 0.01 },
      { prop: 'remark', label: '说明' },
      { prop: 'state', label: '状态', type: 'select', options: [{ label: '有效', value: '1000' }, { label: '失效', value: '2000' }] }
    ]"
    :default-form="{ communityId: cid, discountName: '', discountType: '1001', specValue: 0.9, state: '1000', remark: '' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { DISCOUNT_STATE, DISCOUNT_TYPE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
