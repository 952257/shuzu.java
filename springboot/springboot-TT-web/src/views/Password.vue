<template>
  <div class="page-wrap">
    <div class="page-hero">
      <div>
        <div class="page-kicker">修改密码</div>
        <h2>账号安全</h2>
        <p>修改后需使用新密码重新登录。</p>
      </div>
    </div>
    <div class="panel" style="max-width: 480px">
      <el-form label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="oldPass" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="newPass" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">提交</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import http from "@/api/http";

const router = useRouter();
const oldPass = ref("");
const newPass = ref("");

const save = async () => {
  const res = await http.post("/user.changePassword", { oldPass: oldPass.value, newPass: newPass.value });
  if (res.code === 0) {
    ElMessage.success("密码已修改，请重新登录");
    localStorage.removeItem("tt_token");
    localStorage.removeItem("tt_user");
    router.push("/login");
  }
};
</script>
