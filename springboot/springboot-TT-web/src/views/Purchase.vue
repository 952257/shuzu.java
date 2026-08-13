<template>
  <crud-page
    title="采购申请"
    desc="工程、保洁等物资采购申请，审核通过后可入库。"
    list-api="/purchase.listPurchases"
    save-api="/purchase.savePurchase"
    update-api="/purchase.updatePurchase"
    delete-api="/purchase.deletePurchase"
    delete-key="applyId"
    add-text="新增采购"
    edit-text="编辑采购"
    :op-width="280"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'resourceName', label: '物品名称' }]"
    :columns="[
      { prop: 'resourceName', label: '物品' },
      { prop: 'spec', label: '规格' },
      { prop: 'quantity', label: '数量' },
      { prop: 'price', label: '单价', type: 'money' },
      { prop: 'applyUser', label: '申请人' },
      { prop: 'state', label: '状态', type: 'tag', map: PURCHASE_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'resourceName', label: '物品名称' },
      { prop: 'spec', label: '规格' },
      { prop: 'quantity', label: '数量', type: 'number' },
      { prop: 'price', label: '单价', type: 'number', step: 0.01 },
      { prop: 'applyUser', label: '申请人' },
      { prop: 'remark', label: '备注' }
    ]"
    :default-form="{ communityId: cid, resourceName: '', spec: '', quantity: 1, price: 0, applyUser: '吴学文', state: '1000' }"
    :extra-actions="[
      { label: '通过', type: 'success', api: '/purchase.auditPurchase', body: (row) => ({ applyId: row.applyId, state: '2000' }), msg: '已通过' },
      { label: '拒绝', type: 'danger', api: '/purchase.auditPurchase', body: (row) => ({ applyId: row.applyId, state: '3000' }), msg: '已拒绝' },
      { label: '入库', api: '/purchase.auditPurchase', body: (row) => ({ applyId: row.applyId, state: '4000' }), msg: '已入库' }
    ]"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { PURCHASE_STATE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
