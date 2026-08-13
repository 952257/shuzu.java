<template>
  <div class="hc-shell">
    <header class="topbar glass">
      <div class="brand brand-inline">
        <span class="brand-mark">HC</span>
        <strong>小区物业</strong>
      </div>
      <nav class="top-nav">
        <button :class="{ active: $route.path === '/home' }" @click="go('/home')">首页</button>
        <button :class="{ active: $route.path === '/desk' }" @click="openTab({ path: '/desk', title: '业务受理' })">业务受理</button>
        <button @click="quickOpen = true">常用菜单</button>
        <button :class="{ active: isSettings }" @click="openTab({ path: '/settings', title: '小区配置' })">设置</button>
        <button @click="searchOpen = true">搜索</button>
      </nav>
      <div class="header-right">
        <el-select v-model="communityId" placeholder="切换小区" style="width: 160px" @change="switchCommunity">
          <el-option v-for="c in communities" :key="c.communityId" :label="c.name" :value="c.communityId" />
        </el-select>
        <span class="now">{{ now }}</span>
        <el-dropdown :show-arrow="false">
          <span class="user-chip">
            <el-avatar :size="26">{{ (userName || "U").slice(0, 1) }}</el-avatar>
            {{ userName }} / {{ role === "ADMIN" ? "admin" : "staff" }}
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item disabled>v2.0 · 会话 2 小时</el-dropdown-item>
              <el-dropdown-item @click="openTab({ path: '/password', title: '修改密码' })">修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="work-row">
      <aside class="icon-rail glass">
        <button
          v-for="cat in catalogs"
          :key="cat.id"
          class="rail-item"
          :class="{ active: cat.id === activeCatalog }"
          @click="selectCatalog(cat)"
        >
          <el-icon :size="18"><component :is="cat.icon" /></el-icon>
          <span>{{ cat.title }}</span>
        </button>
      </aside>
      <aside class="sub-nav glass">
        <div class="sub-title">{{ currentCatalog.title }}</div>
        <button
          v-for="item in currentCatalog.children"
          :key="item.path + item.title"
          class="sub-item"
          :class="{ active: $route.path === item.path }"
          @click="openTab(item)"
        >
          {{ item.title }}
        </button>
      </aside>
      <section class="workspace">
        <div class="page-tabs glass">
          <div
            v-for="tab in tabs"
            :key="tab.path"
            class="page-tab"
            :class="{ active: $route.path === tab.path }"
            @click="router.push(tab.path)"
          >
            {{ tab.title }}
            <span class="tab-x" @click.stop="closeTab(tab.path)">×</span>
          </div>
          <button v-if="tabs.length" class="tab-close-all" @click="closeAll">关闭所有</button>
        </div>
        <div class="content">
          <router-view :key="communityId + $route.fullPath" />
        </div>
      </section>
    </div>

    <div v-if="quickOpen" class="quick-mask" @click.self="quickOpen = false">
      <div class="quick-panel glass">
        <button class="quick-close" @click="quickOpen = false">×</button>
        <div class="quick-grid">
          <button v-for="item in quickMenus" :key="item.path" class="quick-card" @click="fromQuick(item)">
            <el-icon :size="28"><component :is="item.icon" /></el-icon>
            <span>{{ item.title }}</span>
          </button>
          <button class="quick-card" @click="ElMessage.info('可把常用页面固定到这里')">
            <el-icon :size="28"><Plus /></el-icon>
            <span>添加</span>
          </button>
        </div>
      </div>
    </div>

    <el-dialog v-model="searchOpen" title="搜索房屋" width="480px">
      <el-input v-model="searchCode" placeholder="楼栋-单元-房屋，如 1-1-101" @keyup.enter="doSearch" />
      <template #footer>
        <el-button @click="searchOpen = false">取消</el-button>
        <el-button type="primary" @click="doSearch">查询并进入业务受理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import http from "@/api/http";
import { getCommunityId, setCommunityId } from "@/utils/community";
import { catalogs, catalogOf, quickMenus } from "./menus";

const router = useRouter();
const route = useRoute();
const user = computed(() => JSON.parse(localStorage.getItem("tt_user") || "{}"));
const userName = computed(() => user.value.userName || "");
const role = computed(() => user.value.role || "");
const now = ref("");
const communityId = ref(getCommunityId());
const communities = ref([]);
const quickOpen = ref(false);
const searchOpen = ref(false);
const searchCode = ref("1-1-101");
const activeCatalog = ref("community");
const tabs = ref([{ path: "/home", title: "首页" }]);
let timer;

const currentCatalog = computed(() => catalogs.find((c) => c.id === activeCatalog.value) || catalogs[0]);
const isSettings = computed(() => ["/settings", "/password", "/logs"].includes(route.path));

const tick = () => {
  now.value = new Date().toLocaleString("zh-CN", { hour12: false });
};

const selectCatalog = (cat) => {
  activeCatalog.value = cat.id;
  if (cat.children[0]) {
    openTab(cat.children[0]);
  }
};

const openTab = (item) => {
  if (!tabs.value.some((t) => t.path === item.path)) {
    tabs.value.push({ path: item.path, title: item.title });
  }
  router.push(item.path);
};

const closeTab = (path) => {
  const idx = tabs.value.findIndex((t) => t.path === path);
  if (idx < 0) {
    return;
  }
  tabs.value.splice(idx, 1);
  if (route.path === path) {
    const next = tabs.value[idx - 1] || tabs.value[0];
    router.push(next ? next.path : "/home");
  }
};

const closeAll = () => {
  tabs.value = [{ path: "/home", title: "首页" }];
  router.push("/home");
};

const go = (path) => router.push(path);

const fromQuick = (item) => {
  quickOpen.value = false;
  openTab(item);
};

const doSearch = () => {
  searchOpen.value = false;
  openTab({ path: "/desk", title: "业务受理" });
  router.push({ path: "/desk", query: { roomCode: searchCode.value } });
};

watch(
  () => route.path,
  (path) => {
    const cat = catalogOf(path);
    if (cat) {
      activeCatalog.value = cat.id;
    }
    if (path === "/settings" || path === "/password" || path === "/logs") {
      activeCatalog.value = "system";
    }
    const known = [...catalogs.flatMap((c) => c.children), { path: "/home", title: "首页" }, { path: "/desk", title: "业务受理" }, { path: "/screen", title: "小区大屏" }];
    const hit = known.find((i) => i.path === path);
    if (hit && !tabs.value.some((t) => t.path === path)) {
      tabs.value.push({ path: hit.path, title: hit.title });
    }
  },
  { immediate: true }
);

onMounted(async () => {
  tick();
  timer = setInterval(tick, 1000);
  const res = await http.get("/community.listCommunitys", { params: { page: 1, row: 50 } });
  communities.value = res.data || [];
});
onUnmounted(() => clearInterval(timer));

const switchCommunity = (id) => {
  setCommunityId(id);
  communityId.value = id;
};

const logout = () => {
  localStorage.removeItem("tt_token");
  localStorage.removeItem("tt_user");
  router.push("/login");
};
</script>
