<template>
  <div class="login-wrap">
    <div class="login-board glass">
      <section class="login-intro">
        <div class="login-badge">HC 小区物业 · TT</div>
        <h1>把小区日常管理<br />收进一张工作台</h1>
        <p>覆盖房产、业主、收费、报修与投诉，按操作手册完成从交房到缴费的完整流程。</p>
        <ul class="login-points">
          <li>楼栋 / 单元 / 房屋 / 车位一体化</li>
          <li>业主认证、账户预存与缴费记录</li>
          <li>报修派单、投诉处理实时可跟</li>
        </ul>
      </section>
      <section class="login-card glass">
        <h2>员工登录</h2>
        <p class="login-sub">会话有效期 2 小时，过期后请重新登录</p>
        <el-form @submit.prevent="login" label-position="top">
          <el-form-item label="账号">
            <el-input v-model="form.username" placeholder="用户名 / 手机号" size="large" :prefix-icon="User" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.passwd" type="password" placeholder="请输入密码" size="large" show-password :prefix-icon="Lock" />
          </el-form-item>
          <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="login">进入系统</el-button>
        </el-form>
        <div class="login-accounts">
          <span>演示账号</span>
          <el-tag effect="plain" @click="fill('admin')">admin / admin</el-tag>
          <el-tag effect="plain" @click="fill('wuxw')">wuxw / admin</el-tag>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { Lock, User } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import http from "@/api/http";

const router = useRouter();
const loading = ref(false);
const form = reactive({ username: "admin", passwd: "admin" });

const fill = (username) => {
  form.username = username;
  form.passwd = "admin";
};

const login = async () => {
  loading.value = true;
  try {
    const res = await http.post("/login.pcUserLogin", form);
    if (res.code !== 0) {
      return;
    }
    localStorage.setItem("tt_token", res.data.token);
    localStorage.setItem("tt_user", JSON.stringify(res.data));
    ElMessage.success("欢迎回来，" + res.data.userName);
    router.push("/home");
  } finally {
    loading.value = false;
  }
};
</script>
