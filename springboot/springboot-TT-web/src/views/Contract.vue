<template>
  <crud-page
    title="合同管理"
    desc="登记物业服务、场地租赁等合同，跟踪履行期限。"
    list-api="/contract.listContracts"
    save-api="/contract.saveContract"
    update-api="/contract.updateContract"
    delete-api="/contract.deleteContract"
    delete-key="contractId"
    add-text="新增合同"
    edit-text="编辑合同"
    :default-query="{ communityId: cid }"
    :query-fields="[{ prop: 'contractName', label: '合同名称' }]"
    :columns="[
      { prop: 'contractCode', label: '编号' },
      { prop: 'contractName', label: '名称', width: 160 },
      { prop: 'contractType', label: '类型', type: 'tag', map: CONTRACT_TYPE },
      { prop: 'partyA', label: '甲方' },
      { prop: 'partyB', label: '乙方' },
      { prop: 'amount', label: '金额', type: 'money' },
      { prop: 'endTime', label: '到期时间', width: 170 },
      { prop: 'state', label: '状态', type: 'tag', map: CONTRACT_STATE }
    ]"
    :form-fields="[
      { prop: 'communityId', label: '小区ID' },
      { prop: 'contractCode', label: '合同编号' },
      { prop: 'contractName', label: '合同名称' },
      { prop: 'contractType', label: '类型', type: 'select', options: [{ label: '物业合同', value: '1001' }, { label: '租赁合同', value: '1002' }] },
      { prop: 'partyA', label: '甲方' },
      { prop: 'partyB', label: '乙方' },
      { prop: 'amount', label: '金额', type: 'number', step: 0.01 },
      { prop: 'startTime', label: '开始时间', type: 'date' },
      { prop: 'endTime', label: '结束时间', type: 'date' }
    ]"
    :default-form="{ communityId: cid, contractCode: '', contractName: '', contractType: '1001', partyA: '业主委员会', partyB: 'HC物业公司', amount: 0, state: '2000' }"
  />
</template>

<script setup>
import CrudPage from "@/components/CrudPage.vue";
import { CONTRACT_STATE, CONTRACT_TYPE } from "@/utils/dict";
import { getCommunityId } from "@/utils/community";
const cid = getCommunityId();
</script>
