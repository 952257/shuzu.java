import axios from "axios";
import { ElMessage } from "element-plus";

const http = axios.create({
  baseURL: "/app",
  timeout: 15000
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("tt_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  (res) => {
    const data = res.data;
    if (data && data.code !== undefined && data.code !== 0) {
      ElMessage.error(data.msg || "请求失败");
    }
    return data;
  },
  async (err) => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem("tt_token");
      localStorage.removeItem("tt_user");
      const { default: router } = await import("@/router");
      if (router.currentRoute.value.path !== "/login") {
        router.push("/login");
      }
    } else {
      ElMessage.error((err.response && err.response.data && err.response.data.msg) || "网络异常");
    }
    return Promise.reject(err);
  }
);

export default http;
