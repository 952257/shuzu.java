const { createApp, ref, reactive, computed, onMounted, watch } = Vue;

const http = axios.create({ baseURL: "/app" });
http.interceptors.request.use((config) => {
    const token = localStorage.getItem("tt_token");
    if (token) {
        config.headers.Authorization = "Bearer " + token;
    }
    return config;
});
http.interceptors.response.use(
    (res) => res.data,
    (err) => {
        if (err.response && err.response.status === 401) {
            localStorage.removeItem("tt_token");
            location.reload();
        }
        return Promise.reject(err);
    }
);

const MENUS = [
    { group: "工作台", items: [{ key: "home", label: "首页概览" }] },
    { group: "基础", items: [{ key: "staff", label: "员工管理" }] },
    {
        group: "资产",
        items: [
            { key: "property", label: "物业公司" },
            { key: "community", label: "小区管理" },
            { key: "floor", label: "楼栋管理" },
            { key: "unit", label: "单元管理" },
            { key: "room", label: "房屋管理" },
            { key: "parking", label: "车位管理" },
            { key: "car", label: "车辆管理" }
        ]
    },
    {
        group: "业主",
        items: [
            { key: "owner", label: "业主信息" },
            { key: "member", label: "家庭成员" },
            { key: "auth", label: "房屋认证" },
            { key: "account", label: "业主账户" }
        ]
    },
    {
        group: "费用",
        items: [
            { key: "feeConfig", label: "费用项" },
            { key: "fee", label: "费用账单" },
            { key: "feeDetail", label: "缴费记录" },
            { key: "meter", label: "水电抄表" }
        ]
    },
    {
        group: "工单",
        items: [
            { key: "repair", label: "报修工单" },
            { key: "complaint", label: "投诉建议" }
        ]
    }
];

const LoginPage = {
    setup(props, { emit }) {
        const form = reactive({ username: "admin", passwd: "admin" });
        const loading = ref(false);
        const login = async () => {
            loading.value = true;
            try {
                const res = await http.post("/login.pcUserLogin", form);
                if (res.code !== 0) {
                    ElementPlus.ElMessage.error(res.msg || "登录失败");
                    return;
                }
                localStorage.setItem("tt_token", res.data.token);
                localStorage.setItem("tt_user", JSON.stringify(res.data));
                emit("ok");
            } catch (e) {
                ElementPlus.ElMessage.error((e.response && e.response.data && e.response.data.msg) || "登录失败");
            } finally {
                loading.value = false;
            }
        };
        return { form, loading, login };
    },
    template: `
    <div class="login-wrap">
      <div class="login-card">
        <h1>TT 小区物业</h1>
        <p>员工登录 · 会话有效期 2 小时</p>
        <el-form @submit.prevent="login">
          <el-form-item><el-input v-model="form.username" placeholder="用户名 / 手机号" size="large"/></el-form-item>
          <el-form-item><el-input v-model="form.passwd" type="password" placeholder="密码" size="large" show-password/></el-form-item>
          <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="login">登录</el-button>
        </el-form>
        <div class="hint">演示账号：admin / admin 或 wuxw / admin</div>
      </div>
    </div>`
};

function useCrud(listApi, extraQuery) {
    const list = ref([]);
    const total = ref(0);
    const page = ref(1);
    const row = ref(10);
    const query = reactive(extraQuery || {});
    const loading = ref(false);
    const load = async () => {
        loading.value = true;
        try {
            const res = await http.get(listApi, { params: { ...query, page: page.value, row: row.value } });
            list.value = res.data || [];
            total.value = res.total || 0;
        } finally {
            loading.value = false;
        }
    };
    onMounted(load);
    return { list, total, page, row, query, loading, load };
}

const HomePage = {
    setup() {
        const stats = ref({});
        onMounted(async () => {
            const res = await http.get("/dashboard.stats");
            stats.value = res.data || {};
        });
        const cards = [
            { key: "communityCount", label: "小区" },
            { key: "roomCount", label: "房屋" },
            { key: "ownerCount", label: "业主" },
            { key: "feeCount", label: "费用单" },
            { key: "repairCount", label: "报修" },
            { key: "complaintCount", label: "投诉" }
        ];
        return { stats, cards };
    },
    template: `
    <div>
      <div class="stats">
        <div class="stat-card" v-for="c in cards" :key="c.key">
          <div class="num">{{ stats[c.key] || 0 }}</div>
          <div class="label">{{ c.label }}</div>
        </div>
      </div>
      <div class="panel">
        <h3>使用说明</h3>
        <p>按操作手册顺序维护：物业公司 → 小区 → 楼栋 → 单元 → 房屋 → 业主 → 交房 → 费用项 → 创建费用 → 缴费。</p>
        <p>报修与投诉可在工单菜单处理。房屋认证提交后可在认证审核中通过或拒绝。</p>
      </div>
    </div>`
};

function crudPage(options) {
    return {
        setup() {
            const state = useCrud(options.listApi, options.query || {});
            const dialog = ref(false);
            const isEdit = ref(false);
            const form = reactive({ ...(options.empty || {}) });
            const reset = () => Object.assign(form, options.empty || {}, {});
            const openAdd = () => { isEdit.value = false; Object.keys(form).forEach(k => delete form[k]); Object.assign(form, JSON.parse(JSON.stringify(options.empty || {}))); dialog.value = true; };
            const openEdit = (row) => { isEdit.value = true; Object.keys(form).forEach(k => delete form[k]); Object.assign(form, JSON.parse(JSON.stringify(row))); dialog.value = true; };
            const save = async () => {
                const api = isEdit.value ? options.updateApi : options.saveApi;
                const res = await http.post(api, form);
                if (res.code === 0) {
                    ElementPlus.ElMessage.success("成功");
                    dialog.value = false;
                    state.load();
                } else {
                    ElementPlus.ElMessage.error(res.msg);
                }
            };
            const remove = async (idObj) => {
                await ElementPlus.ElMessageBox.confirm("确认删除？", "提示");
                const res = await http.post(options.deleteApi, idObj);
                if (res.code === 0) { ElementPlus.ElMessage.success("已删除"); state.load(); }
                else ElementPlus.ElMessage.error(res.msg);
            };
            const action = async (api, body, msg) => {
                const res = await http.post(api, body);
                if (res.code === 0) { ElementPlus.ElMessage.success(msg || "成功"); state.load(); }
                else ElementPlus.ElMessage.error(res.msg);
            };
            return { ...state, dialog, isEdit, form, openAdd, openEdit, save, remove, action };
        },
        template: options.template
    };
}

const StaffPage = crudPage({
    listApi: "/query.staff.infos",
    saveApi: "/user.staff.add",
    updateApi: "/user.staff.modify",
    deleteApi: "/user.staff.delete",
    empty: { name: "", username: "", tel: "", password: "admin", role: "STAFF" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="姓名" style="width:160px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">新增员工</el-button>
      </div>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column prop="tel" label="电话"/>
        <el-table-column prop="role" label="角色"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({userId: row.userId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" :title="isEdit ? '编辑员工' : '新增员工'" width="480px">
        <el-form label-width="80px">
          <el-form-item label="姓名"><el-input v-model="form.name"/></el-form-item>
          <el-form-item label="用户名"><el-input v-model="form.username"/></el-form-item>
          <el-form-item label="电话"><el-input v-model="form.tel"/></el-form-item>
          <el-form-item label="密码"><el-input v-model="form.password" placeholder="不改请留空"/></el-form-item>
          <el-form-item label="角色">
            <el-select v-model="form.role"><el-option label="管理员" value="ADMIN"/><el-option label="员工" value="STAFF"/></el-select>
          </el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const PropertyPage = crudPage({
    listApi: "/property.listProperty",
    saveApi: "/property.saveProperty",
    updateApi: "/property.updateProperty",
    deleteApi: "/property.deleteProperty",
    empty: { name: "", tel: "", address: "", nearbyLandmarks: "", corporation: "", foundingTime: "" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="物业名称" style="width:180px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加物业</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="storeId" label="编号" width="160"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="tel" label="电话"/>
        <el-table-column prop="address" label="地址"/>
        <el-table-column prop="corporation" label="法人"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({storeId: row.storeId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" :title="isEdit?'修改物业':'添加物业'" width="520px">
        <el-form label-width="90px">
          <el-form-item label="名称"><el-input v-model="form.name"/></el-form-item>
          <el-form-item label="电话"><el-input v-model="form.tel"/></el-form-item>
          <el-form-item label="地址"><el-input v-model="form.address"/></el-form-item>
          <el-form-item label="地标"><el-input v-model="form.nearbyLandmarks"/></el-form-item>
          <el-form-item label="法人"><el-input v-model="form.corporation"/></el-form-item>
          <el-form-item label="成立日期"><el-input v-model="form.foundingTime" placeholder="2010-01-01"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const CommunityPage = crudPage({
    listApi: "/community.listCommunitys",
    saveApi: "/community.saveCommunity",
    updateApi: "/community.updateCommunity",
    deleteApi: "/community.deleteCommunity",
    empty: { name: "", address: "", cityCode: "630104", cityName: "青海省西宁市城西区", nearbyLandmarks: "国投广场", tel: "", payFeeMonth: 12, feePrice: 0 },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="小区名称" style="width:180px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加小区</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="communityId" label="小区ID" width="170"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="address" label="地址"/>
        <el-table-column prop="tel" label="电话"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({communityId: row.communityId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" :title="isEdit?'修改小区':'添加小区'" width="560px">
        <el-form label-width="90px">
          <el-form-item label="名称"><el-input v-model="form.name"/></el-form-item>
          <el-form-item label="地址"><el-input v-model="form.address"/></el-form-item>
          <el-form-item label="地区编码"><el-input v-model="form.cityCode"/></el-form-item>
          <el-form-item label="城市"><el-input v-model="form.cityName"/></el-form-item>
          <el-form-item label="地标"><el-input v-model="form.nearbyLandmarks"/></el-form-item>
          <el-form-item label="电话"><el-input v-model="form.tel"/></el-form-item>
          <el-form-item label="收费周期"><el-input-number v-model="form.payFeeMonth"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const FloorPage = crudPage({
    listApi: "/floor.queryFloors",
    saveApi: "/floor.saveFloor",
    updateApi: "/floor.editFloor",
    deleteApi: "/floor.deleteFloor",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", floorNum: "", name: "", floorArea: 0, seq: 1 },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.communityId" placeholder="小区ID" style="width:200px"/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加楼栋</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="floorNum" label="编号"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="floorArea" label="面积"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({floorId: row.floorId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" :title="isEdit?'编辑楼栋':'添加楼栋'" width="480px">
        <el-form label-width="90px">
          <el-form-item label="小区ID"><el-input v-model="form.communityId"/></el-form-item>
          <el-form-item label="编号"><el-input v-model="form.floorNum"/></el-form-item>
          <el-form-item label="名称"><el-input v-model="form.name"/></el-form-item>
          <el-form-item label="面积"><el-input-number v-model="form.floorArea"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const UnitPage = crudPage({
    listApi: "/unit.queryUnits",
    saveApi: "/unit.saveUnit",
    updateApi: "/unit.updateUnit",
    deleteApi: "/unit.deleteUnit",
    query: { floorId: "3022081500000001" },
    empty: { floorId: "3022081500000001", unitNum: "", layerCount: 6, lift: "1010", unitArea: 0 },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.floorId" placeholder="楼栋ID" style="width:200px"/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加单元</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="unitNum" label="单元号"/>
        <el-table-column prop="layerCount" label="层数"/>
        <el-table-column prop="lift" label="电梯(1010有/2020无)"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({unitId: row.unitId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" :title="isEdit?'编辑单元':'添加单元'" width="480px">
        <el-form label-width="90px">
          <el-form-item label="楼栋ID"><el-input v-model="form.floorId"/></el-form-item>
          <el-form-item label="单元号"><el-input v-model="form.unitNum"/></el-form-item>
          <el-form-item label="层数"><el-input-number v-model="form.layerCount"/></el-form-item>
          <el-form-item label="电梯">
            <el-select v-model="form.lift"><el-option label="有电梯" value="1010"/><el-option label="无电梯" value="2020"/></el-select>
          </el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const RoomPage = crudPage({
    listApi: "/room.queryRooms",
    saveApi: "/room.saveRoom",
    updateApi: "/room.updateRoom",
    deleteApi: "/room.deleteRoom",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", unitId: "4022081500000001", roomNum: "", layer: "1", apartment: "两室一厅", builtUpArea: 90, roomArea: 80, state: "2001" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.roomNum" placeholder="房屋编号" style="width:140px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加房屋</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="roomNum" label="房号"/>
        <el-table-column prop="layer" label="楼层"/>
        <el-table-column prop="apartment" label="户型"/>
        <el-table-column prop="builtUpArea" label="建筑面积"/>
        <el-table-column prop="state" label="状态(2001未售/2002已售)"/>
        <el-table-column label="操作" width="260">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="success" @click="action('/room.sellRoom', {roomId: row.roomId, ownerId: '6022081500000001'}, '交房成功')">交房</el-button>
            <el-button link type="warning" @click="action('/room.exitRoom', {roomId: row.roomId, ownerId: '6022081500000001'}, '已退房')">退房</el-button>
            <el-button link type="danger" @click="remove({roomId: row.roomId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" :title="isEdit?'修改房屋':'添加房屋'" width="520px">
        <el-form label-width="90px">
          <el-form-item label="小区ID"><el-input v-model="form.communityId"/></el-form-item>
          <el-form-item label="单元ID"><el-input v-model="form.unitId"/></el-form-item>
          <el-form-item label="房号"><el-input v-model="form.roomNum"/></el-form-item>
          <el-form-item label="楼层"><el-input v-model="form.layer"/></el-form-item>
          <el-form-item label="户型"><el-input v-model="form.apartment"/></el-form-item>
          <el-form-item label="建筑面积"><el-input-number v-model="form.builtUpArea"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const OwnerPage = crudPage({
    listApi: "/owner.queryOwners",
    saveApi: "/owner.saveOwner",
    updateApi: "/owner.editOwner",
    deleteApi: "/owner.deleteOwner",
    query: { communityId: "2022081539020475", ownerTypeCd: "1001" },
    empty: { communityId: "2022081539020475", name: "", link: "", idCard: "", sex: "0", ownerTypeCd: "1001", personRole: "1", address: "" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="业主姓名" style="width:160px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加业主</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="link" label="手机"/>
        <el-table-column prop="idCard" label="证件号"/>
        <el-table-column prop="address" label="住址"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({memberId: row.memberId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" :title="isEdit?'编辑业主':'添加业主'" width="520px">
        <el-form label-width="90px">
          <el-form-item label="姓名"><el-input v-model="form.name"/></el-form-item>
          <el-form-item label="手机"><el-input v-model="form.link"/></el-form-item>
          <el-form-item label="证件号"><el-input v-model="form.idCard"/></el-form-item>
          <el-form-item label="住址"><el-input v-model="form.address"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const MemberPage = crudPage({
    listApi: "/owner.queryOwners",
    saveApi: "/owner.saveOwner",
    updateApi: "/owner.editOwner",
    deleteApi: "/owner.deleteOwner",
    query: { communityId: "2022081539020475", ownerTypeCd: "1002" },
    empty: { communityId: "2022081539020475", ownerId: "6022081500000001", name: "", link: "", ownerTypeCd: "1002", personRole: "3" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.name" placeholder="成员姓名" style="width:160px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加成员</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="link" label="手机"/>
        <el-table-column prop="ownerId" label="所属业主ID"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({memberId: row.memberId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="家庭成员" width="480px">
        <el-form label-width="100px">
          <el-form-item label="业主ID"><el-input v-model="form.ownerId"/></el-form-item>
          <el-form-item label="姓名"><el-input v-model="form.name"/></el-form-item>
          <el-form-item label="手机"><el-input v-model="form.link"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const AuthPage = crudPage({
    listApi: "/owner.listAppUserBindingOwners",
    saveApi: "/owner.saveOwnerAppUser",
    updateApi: "/owner.saveOwnerAppUser",
    deleteApi: "/owner.saveOwnerAppUser",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", appUserName: "", link: "", idCard: "", roomName: "1-1-101", memberId: "6022081500000001" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-button type="primary" @click="load">刷新</el-button>
        <el-button @click="openAdd">提交认证</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="appUserName" label="姓名"/>
        <el-table-column prop="link" label="手机"/>
        <el-table-column prop="roomName" label="房屋"/>
        <el-table-column prop="state" label="状态(10000待审/12000通过/13000拒绝)"/>
        <el-table-column label="审核" width="200">
          <template #default="{row}">
            <el-button link type="success" @click="action('/owner.auditAuthOwner', {appUserId: row.appUserId, state: '12000'}, '已通过')">通过</el-button>
            <el-button link type="danger" @click="action('/owner.auditAuthOwner', {appUserId: row.appUserId, state: '13000', remark: '资料不符'}, '已拒绝')">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="房屋认证" width="480px">
        <el-form label-width="90px">
          <el-form-item label="姓名"><el-input v-model="form.appUserName"/></el-form-item>
          <el-form-item label="手机"><el-input v-model="form.link"/></el-form-item>
          <el-form-item label="证件号"><el-input v-model="form.idCard"/></el-form-item>
          <el-form-item label="房屋"><el-input v-model="form.roomName"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">提交</el-button></template>
      </el-dialog>
    </div>`
});

const ParkingPage = crudPage({
    listApi: "/parkingSpace.queryParkingSpaces",
    saveApi: "/parkingSpace.saveParkingSpace",
    updateApi: "/parkingSpace.editParkingSpace",
    deleteApi: "/parkingSpace.deleteParkingSpace",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", num: "", parkingType: "1", state: "F", area: 12 },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.num" placeholder="车位号" style="width:140px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加车位</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="num" label="车位号"/>
        <el-table-column prop="parkingType" label="类型(1地上/2地下)"/>
        <el-table-column prop="state" label="状态(F空闲/S已售/H出租)"/>
        <el-table-column label="操作" width="260">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="success" @click="action('/parkingSpace.sellParkingSpace', {psId: row.psId}, '已出售')">出售</el-button>
            <el-button link type="warning" @click="action('/parkingSpace.exitParkingSpace', {psId: row.psId}, '已退还')">退还</el-button>
            <el-button link type="danger" @click="remove({psId: row.psId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="车位" width="480px">
        <el-form label-width="90px">
          <el-form-item label="车位号"><el-input v-model="form.num"/></el-form-item>
          <el-form-item label="类型"><el-select v-model="form.parkingType"><el-option label="地上" value="1"/><el-option label="地下" value="2"/></el-select></el-form-item>
          <el-form-item label="面积"><el-input-number v-model="form.area"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const CarPage = crudPage({
    listApi: "/owner.listOwnerCars",
    saveApi: "/owner.saveOwnerCar",
    updateApi: "/owner.updateOwnerCar",
    deleteApi: "/owner.deleteOwnerCar",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", ownerId: "6022081500000001", carNum: "", carBrand: "", carColor: "", psId: "" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.carNum" placeholder="车牌号" style="width:160px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加车辆</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="carNum" label="车牌"/>
        <el-table-column prop="carBrand" label="品牌"/>
        <el-table-column prop="carColor" label="颜色"/>
        <el-table-column prop="ownerId" label="业主ID"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({carId: row.carId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="车辆" width="480px">
        <el-form label-width="90px">
          <el-form-item label="业主ID"><el-input v-model="form.ownerId"/></el-form-item>
          <el-form-item label="车牌"><el-input v-model="form.carNum"/></el-form-item>
          <el-form-item label="品牌"><el-input v-model="form.carBrand"/></el-form-item>
          <el-form-item label="颜色"><el-input v-model="form.carColor"/></el-form-item>
          <el-form-item label="车位ID"><el-input v-model="form.psId"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const FeeConfigPage = crudPage({
    listApi: "/feeConfig.listFeeConfigs",
    saveApi: "/feeConfig.saveFeeConfig",
    updateApi: "/feeConfig.updateFeeConfig",
    deleteApi: "/feeConfig.deleteFeeConfig",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", feeTypeCd: "888800010001", feeName: "物业费", feeFlag: "1003006", squarePrice: 1.5, additionalAmount: 0, paymentCycle: "12" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.feeName" placeholder="费用项" style="width:160px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">添加费用项</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="feeName" label="名称"/>
        <el-table-column prop="feeTypeCd" label="类型"/>
        <el-table-column prop="squarePrice" label="单价"/>
        <el-table-column prop="paymentCycle" label="周期(月)"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({configId: row.configId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="费用项" width="480px">
        <el-form label-width="90px">
          <el-form-item label="名称"><el-input v-model="form.feeName"/></el-form-item>
          <el-form-item label="类型编码"><el-input v-model="form.feeTypeCd"/></el-form-item>
          <el-form-item label="单价"><el-input-number v-model="form.squarePrice" :step="0.1"/></el-form-item>
          <el-form-item label="周期"><el-input v-model="form.paymentCycle"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const FeePage = crudPage({
    listApi: "/fee.listFee",
    saveApi: "/fee.saveFee",
    updateApi: "/fee.saveFee",
    deleteApi: "/fee.deleteFee",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", configId: "A022081500000001", payerObjId: "5022081500000001", payerObjType: "3333", amount: 134.25 },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">创建费用</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="feeName" label="费用"/>
        <el-table-column prop="payerObjId" label="缴费对象"/>
        <el-table-column prop="amount" label="金额"/>
        <el-table-column prop="state" label="状态(2008001收费中/2009001结束)"/>
        <el-table-column label="操作" width="220">
          <template #default="{row}">
            <el-button link type="success" @click="action('/fee.payFee', {feeId: row.feeId, receivedAmount: row.amount, cycles: 1}, '缴费成功')">缴费</el-button>
            <el-button link type="warning" @click="remove({feeId: row.feeId})">结束费用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="创建费用" width="480px">
        <el-form label-width="100px">
          <el-form-item label="费用项ID"><el-input v-model="form.configId"/></el-form-item>
          <el-form-item label="房屋/车位ID"><el-input v-model="form.payerObjId"/></el-form-item>
          <el-form-item label="金额"><el-input-number v-model="form.amount" :step="0.01"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const FeeDetailPage = crudPage({
    listApi: "/fee.queryFeeDetail",
    saveApi: "/fee.payFee",
    updateApi: "/fee.payFee",
    deleteApi: "/fee.payFee",
    query: { communityId: "2022081539020475" },
    empty: {},
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.feeId" placeholder="费用ID" style="width:200px" clearable/>
        <el-button type="primary" @click="load">查询缴费记录</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="feeId" label="费用ID"/>
        <el-table-column prop="receivedAmount" label="实收"/>
        <el-table-column prop="receivableAmount" label="应收"/>
        <el-table-column prop="payTime" label="缴费时间"/>
        <el-table-column prop="state" label="状态"/>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
    </div>`
});

const MeterPage = crudPage({
    listApi: "/meterWater.listMeterWaters",
    saveApi: "/meterWater.saveMeterWater",
    updateApi: "/meterWater.updateMeterWater",
    deleteApi: "/meterWater.deleteMeterWater",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", objId: "5022081500000001", meterType: "2020", preDegrees: 0, curDegrees: 10 },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-select v-model="query.meterType" clearable placeholder="表类型" style="width:140px">
          <el-option label="水表" value="2020"/><el-option label="电表" value="3030"/>
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">抄表</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="objId" label="房屋ID"/>
        <el-table-column prop="meterType" label="类型"/>
        <el-table-column prop="preDegrees" label="上期读数"/>
        <el-table-column prop="curDegrees" label="本期读数"/>
        <el-table-column label="操作" width="180">
          <template #default="{row}">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove({waterId: row.waterId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="水电抄表" width="480px">
        <el-form label-width="90px">
          <el-form-item label="房屋ID"><el-input v-model="form.objId"/></el-form-item>
          <el-form-item label="类型"><el-select v-model="form.meterType"><el-option label="水表" value="2020"/><el-option label="电表" value="3030"/></el-select></el-form-item>
          <el-form-item label="上期"><el-input-number v-model="form.preDegrees"/></el-form-item>
          <el-form-item label="本期"><el-input-number v-model="form.curDegrees"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const RepairPage = crudPage({
    listApi: "/repair.listRepairs",
    saveApi: "/repair.saveRepair",
    updateApi: "/repair.updateRepair",
    deleteApi: "/repair.deleteRepair",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", repairName: "", tel: "", context: "", repairObjName: "1-1-101" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-input v-model="query.repairName" placeholder="报修人" style="width:160px" clearable/>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">登记报修</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="repairName" label="报修人"/>
        <el-table-column prop="tel" label="电话"/>
        <el-table-column prop="context" label="内容"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column prop="staffName" label="处理人"/>
        <el-table-column label="操作" width="240">
          <template #default="{row}">
            <el-button link type="primary" @click="action('/repair.dispatchRepair', {repairId: row.repairId, staffId: '1000000002', staffName: '吴学文'}, '已派单')">派单</el-button>
            <el-button link type="success" @click="action('/repair.finishRepair', {repairId: row.repairId}, '已完成')">完成</el-button>
            <el-button link type="danger" @click="remove({repairId: row.repairId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="报修" width="480px">
        <el-form label-width="90px">
          <el-form-item label="报修人"><el-input v-model="form.repairName"/></el-form-item>
          <el-form-item label="电话"><el-input v-model="form.tel"/></el-form-item>
          <el-form-item label="位置"><el-input v-model="form.repairObjName"/></el-form-item>
          <el-form-item label="内容"><el-input type="textarea" v-model="form.context"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const ComplaintPage = crudPage({
    listApi: "/complaint.listComplaints",
    saveApi: "/complaint.saveComplaint",
    updateApi: "/complaint.updateComplaint",
    deleteApi: "/complaint.deleteComplaint",
    query: { communityId: "2022081539020475" },
    empty: { communityId: "2022081539020475", typeCd: "809001", complaintName: "", tel: "", context: "" },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-select v-model="query.typeCd" clearable placeholder="类型" style="width:140px">
          <el-option label="投诉" value="809001"/><el-option label="建议" value="809002"/>
        </el-select>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button @click="openAdd">登记投诉</el-button>
      </div>
      <el-table :data="list" stripe>
        <el-table-column prop="complaintName" label="投诉人"/>
        <el-table-column prop="typeCd" label="类型"/>
        <el-table-column prop="context" label="内容"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column label="操作" width="200">
          <template #default="{row}">
            <el-button link type="success" @click="action('/complaint.auditComplaint', {complaintId: row.complaintId}, '已处理')">处理</el-button>
            <el-button link type="danger" @click="remove({complaintId: row.complaintId})">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" layout="total, prev, pager, next" :total="total" v-model:current-page="page" :page-size="row" @current-change="load"/>
      <el-dialog v-model="dialog" title="投诉建议" width="480px">
        <el-form label-width="90px">
          <el-form-item label="姓名"><el-input v-model="form.complaintName"/></el-form-item>
          <el-form-item label="电话"><el-input v-model="form.tel"/></el-form-item>
          <el-form-item label="类型"><el-select v-model="form.typeCd"><el-option label="投诉" value="809001"/><el-option label="建议" value="809002"/></el-select></el-form-item>
          <el-form-item label="内容"><el-input type="textarea" v-model="form.context"/></el-form-item>
        </el-form>
        <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
      </el-dialog>
    </div>`
});

const AccountPage = {
    setup() {
        const state = useCrud("/account.queryOwnerAccount", { communityId: "2022081539020475" });
        const details = ref([]);
        const amount = ref(100);
        const loadDetail = async (acctId) => {
            const res = await http.get("/account.listAccountDetail", { params: { acctId, page: 1, row: 50 } });
            details.value = res.data || [];
        };
        const prestore = async (acctId) => {
            const res = await http.post("/account.ownerPrestoreAccount", { acctId, amount: amount.value, remark: "前台预存" });
            if (res.code === 0) { ElementPlus.ElMessage.success("预存成功"); state.load(); loadDetail(acctId); }
            else ElementPlus.ElMessage.error(res.msg);
        };
        const cancel = async (detailId, acctId) => {
            const res = await http.post("/account.cancelAccountDetail", { detailId });
            if (res.code === 0) { ElementPlus.ElMessage.success("已撤销"); state.load(); loadDetail(acctId); }
            else ElementPlus.ElMessage.error(res.msg);
        };
        return { ...state, details, amount, loadDetail, prestore, cancel };
    },
    template: `
    <div class="panel">
      <div class="toolbar">
        <el-button type="primary" @click="load">刷新账户</el-button>
        <el-input-number v-model="amount" :min="1"/>
      </div>
      <el-table :data="list" stripe @row-click="(row)=>loadDetail(row.acctId)">
        <el-table-column prop="acctName" label="账户"/>
        <el-table-column prop="objId" label="业主ID"/>
        <el-table-column prop="amount" label="余额"/>
        <el-table-column label="操作" width="160">
          <template #default="{row}">
            <el-button link type="success" @click.stop="prestore(row.acctId)">预存</el-button>
            <el-button link @click.stop="loadDetail(row.acctId)">明细</el-button>
          </template>
        </el-table-column>
      </el-table>
      <h4>账户明细</h4>
      <el-table :data="details" stripe>
        <el-table-column prop="detailType" label="类型(1001转入/2002转出/3003撤销)"/>
        <el-table-column prop="amount" label="金额"/>
        <el-table-column prop="remark" label="备注"/>
        <el-table-column prop="state" label="状态"/>
        <el-table-column label="操作">
          <template #default="{row}">
            <el-button v-if="row.state!=='2002'" link type="danger" @click="cancel(row.detailId, row.acctId)">撤销</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>`
};

const PAGES = {
    home: HomePage,
    staff: StaffPage,
    property: PropertyPage,
    community: CommunityPage,
    floor: FloorPage,
    unit: UnitPage,
    room: RoomPage,
    owner: OwnerPage,
    member: MemberPage,
    auth: AuthPage,
    parking: ParkingPage,
    car: CarPage,
    feeConfig: FeeConfigPage,
    fee: FeePage,
    feeDetail: FeeDetailPage,
    meter: MeterPage,
    repair: RepairPage,
    complaint: ComplaintPage,
    account: AccountPage
};

const App = {
    components: { LoginPage, ...PAGES },
    setup() {
        const token = ref(localStorage.getItem("tt_token"));
        const user = ref(JSON.parse(localStorage.getItem("tt_user") || "null"));
        const page = ref("home");
        const current = computed(() => PAGES[page.value] || HomePage);
        const onLogin = () => {
            token.value = localStorage.getItem("tt_token");
            user.value = JSON.parse(localStorage.getItem("tt_user") || "null");
        };
        const logout = () => {
            localStorage.removeItem("tt_token");
            localStorage.removeItem("tt_user");
            token.value = null;
        };
        return { token, user, page, current, menus: MENUS, onLogin, logout };
    },
    template: `
    <login-page v-if="!token" @ok="onLogin"/>
    <div class="layout" v-else>
      <aside class="sidebar">
        <div class="brand">TT 小区物业</div>
        <div class="menu">
          <div v-for="g in menus" :key="g.group">
            <div class="menu-group">{{ g.group }}</div>
            <div class="menu-item" v-for="it in g.items" :key="it.key" :class="{active: page===it.key}" @click="page=it.key">{{ it.label }}</div>
          </div>
        </div>
      </aside>
      <section class="main">
        <header class="header">
          <div>当前用户：{{ user && user.userName }}（{{ user && user.role }}）</div>
          <el-button @click="logout">退出</el-button>
        </header>
        <div class="content"><component :is="current"/></div>
      </section>
    </div>`
};

createApp(App).use(ElementPlus).mount("#app");
