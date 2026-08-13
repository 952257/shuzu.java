<template>
  <div class="page-wrap">
    <div class="page-hero">
      <div>
        <div class="page-kicker">{{ kicker || "物业工作台" }}</div>
        <h2>{{ title }}</h2>
        <p v-if="desc">{{ desc }}</p>
      </div>
      <div class="page-hero-extra">
        <slot name="extra" />
      </div>
    </div>
    <div class="panel crud-panel">
      <div class="toolbar">
        <template v-for="field in queryFields" :key="field.prop">
          <el-input
            v-if="field.type !== 'select' && field.type !== 'number'"
            v-model="query[field.prop]"
            :placeholder="field.label"
            clearable
            style="width: 180px"
            @keyup.enter="load"
          />
          <el-select
            v-else-if="field.type === 'select'"
            v-model="query[field.prop]"
            :placeholder="field.label"
            clearable
            style="width: 160px"
          >
            <el-option v-for="opt in field.options" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </template>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        <div style="flex: 1"></div>
        <el-button v-if="saveApi" type="success" :icon="Plus" @click="openAdd">{{ addText }}</el-button>
      </div>
      <el-table :data="list" v-loading="loading" stripe border empty-text="暂无数据">
        <el-table-column v-for="col in columns" :key="col.prop" :prop="col.prop" :label="col.label" :min-width="col.width || 120" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tag v-if="col.type === 'tag' && col.map" :type="tagOf(col.map, row[col.prop]).type" effect="light" round>
              {{ tagOf(col.map, row[col.prop]).label }}
            </el-tag>
            <span v-else-if="col.type === 'money'" class="money">¥ {{ Number(row[col.prop] || 0).toFixed(2) }}</span>
            <span v-else>{{ row[col.prop] ?? "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="showOps" label="操作" :width="opWidth" fixed="right">
          <template #default="{ row }">
            <el-button v-if="updateApi" link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button
              v-for="act in extraActions"
              :key="act.label"
              link
              :type="act.type || 'primary'"
              @click="runAction(act, row)"
            >
              {{ act.label }}
            </el-button>
            <el-button v-if="deleteApi" link type="danger" @click="remove(row)">{{ deleteText }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-footer">
        <span class="total-tip">共 {{ total }} 条记录</span>
        <el-pagination
          layout="sizes, prev, pager, next, jumper"
          :total="total"
          v-model:current-page="page"
          v-model:page-size="row"
          :page-sizes="[10, 20, 50]"
          @current-change="load"
          @size-change="load"
        />
      </div>
    </div>
    <el-dialog v-model="dialog" :title="isEdit ? editText : addText" width="560px" destroy-on-close>
      <el-form label-width="108px" class="dialog-form">
        <el-form-item v-for="field in formFields" :key="field.prop" :label="field.label">
          <el-input v-if="!field.type || field.type === 'input'" v-model="form[field.prop]" :placeholder="field.placeholder || '请输入' + field.label" />
          <el-input v-else-if="field.type === 'textarea'" type="textarea" :rows="3" v-model="form[field.prop]" />
          <el-input-number v-else-if="field.type === 'number'" v-model="form[field.prop]" :step="field.step || 1" style="width: 100%" />
          <el-date-picker
            v-else-if="field.type === 'date'"
            v-model="form[field.prop]"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
          <el-select v-else-if="field.type === 'select'" v-model="form[field.prop]" style="width: 100%">
            <el-option v-for="opt in field.options" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { Plus, Refresh, Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import http from "@/api/http";
import { getCommunityId } from "@/utils/community";

const props = defineProps({
  title: String,
  kicker: String,
  desc: String,
  listApi: { type: String, required: true },
  saveApi: String,
  updateApi: String,
  deleteApi: String,
  deleteKey: String,
  addText: { type: String, default: "新增" },
  editText: { type: String, default: "编辑" },
  queryFields: { type: Array, default: () => [] },
  defaultQuery: { type: Object, default: () => ({}) },
  columns: { type: Array, default: () => [] },
  formFields: { type: Array, default: () => [] },
  defaultForm: { type: Object, default: () => ({}) },
  extraActions: { type: Array, default: () => [] },
  opWidth: { type: Number, default: 220 },
  deleteText: { type: String, default: "删除" }
});

const emit = defineEmits(["loaded"]);

const list = ref([]);
const total = ref(0);
const page = ref(1);
const row = ref(10);
const loading = ref(false);
const dialog = ref(false);
const isEdit = ref(false);
const query = reactive({ ...props.defaultQuery });
const form = reactive({});

const showOps = computed(() => props.updateApi || props.deleteApi || props.extraActions.length);

const tagOf = (map, value) => map[value] || { label: value || "-", type: "info" };

const emptyForm = () => {
  Object.keys(form).forEach((k) => delete form[k]);
  Object.assign(form, JSON.parse(JSON.stringify(props.defaultForm)));
  if (Object.prototype.hasOwnProperty.call(form, "communityId")) {
    form.communityId = getCommunityId();
  }
};

const load = async () => {
  loading.value = true;
  try {
    if (Object.prototype.hasOwnProperty.call(props.defaultQuery, "communityId")) {
      query.communityId = getCommunityId();
    }
    const res = await http.get(props.listApi, { params: { ...query, page: page.value, row: row.value } });
    if (res.code === 0) {
      list.value = res.data || [];
      total.value = res.total || 0;
      emit("loaded", list.value);
    }
  } finally {
    loading.value = false;
  }
};

const resetQuery = () => {
  Object.keys(query).forEach((k) => delete query[k]);
  Object.assign(query, JSON.parse(JSON.stringify(props.defaultQuery)));
  page.value = 1;
  load();
};

const openAdd = () => {
  isEdit.value = false;
  emptyForm();
  dialog.value = true;
};

const openEdit = (rowData) => {
  isEdit.value = true;
  emptyForm();
  Object.assign(form, JSON.parse(JSON.stringify(rowData)));
  dialog.value = true;
};

const save = async () => {
  const api = isEdit.value ? props.updateApi : props.saveApi;
  const res = await http.post(api, form);
  if (res.code === 0) {
    ElMessage.success("保存成功");
    dialog.value = false;
    load();
  }
};

const remove = async (rowData) => {
  await ElMessageBox.confirm(`确认${props.deleteText}这条记录？`, "提示", { type: "warning" });
  const body = props.deleteKey ? { [props.deleteKey]: rowData[props.deleteKey] } : rowData;
  const res = await http.post(props.deleteApi, body);
  if (res.code === 0) {
    ElMessage.success("操作成功");
    load();
  }
};

const runAction = async (act, rowData) => {
  let body = typeof act.body === "function" ? act.body(rowData) : { ...act.body, ...rowData };
  if (act.prompt) {
    const { value } = await ElMessageBox.prompt(act.prompt.message, act.prompt.title || act.label);
    body = { ...body, [act.prompt.key]: value };
  }
  if (act.confirm) {
    await ElMessageBox.confirm(act.confirm, "提示");
  }
  const res = await http.post(act.api, body);
  if (res.code === 0) {
    ElMessage.success(act.msg || "成功");
    load();
  }
};

onMounted(load);
defineExpose({ load, openAdd });
</script>
