# TT 小区物业管理系统

面向物业公司的后台管理系统，覆盖小区资产、业主、费用和工单。接口风格对齐 [HC 小区物业接口](http://api.homecommunity.cn/pages/hc/login/PcUserLoginCmd_cn.html)，**不做商城、不做物联网**，请求头只用 `Authorization: Bearer {token}`，没有 APP-ID / SIGN。

| 端 | 目录 | 访问地址 |
|---|---|---|
| 后端 | `springboot/springboot-TT` | http://localhost:8088 |
| 前端 | `springboot/springboot-TT-web` | http://localhost:5173 |
| 接口用例 | `springboot/springboot-TT/apifox` | 导入 Apifox |

前端开发时通过 Vite 把 `/app` 代理到 `8088`，浏览器只开 **5173** 即可。

---

## 技术栈

- 后端：Spring Boot 2.6.2、Java 17、MyBatis-Plus 3.5.6、MySQL、JWT
- 前端：Vue 3、Vite、Vue Router、Element Plus、Axios
- 启动类：`com.tt.TtApplication`

---

## 目录结构

```
springboot-TT/                         后端
├── apifox/                            Apifox / Postman 用例
│   ├── TT-物业接口.postman_collection.json
│   ├── TT-物业环境.postman_environment.json
│   └── generate_collection.py
├── src/main/java/com/tt/
│   ├── TtApplication.java
│   ├── common/                        统一返回、异常、JWT、分页
│   ├── config/                        MyBatis-Plus、拦截器、CORS
│   ├── controller/                    /app/{service.action}
│   ├── interceptor/AuthInterceptor.java
│   ├── mapper/  po/  service/  dto/
└── src/main/resources/
    ├── application.yml                激活 dev
    ├── application-dev.yml            端口、数据源、JWT
    └── db/schema.sql                  建表 + 演示数据（含 DROP，慎用）

springboot-TT-web/                     前端
├── src/api/http.js                    Axios，自动带 token
├── src/layout/                        壳子、菜单
├── src/views/                         业务页面
└── vite.config.js                     代理 /app → 8088
```

---

## 环境要求

- JDK 17
- Maven 3.6+
- Node.js 18+（前端）
- MySQL 8，库名 `TT`，账号见 `src/main/resources/application-dev.yml`

本机 IDEA 运行时，`JAVA_HOME` 需指向 JDK 17。

---

## 数据库

配置文件：`src/main/resources/application-dev.yml`

```
jdbc:mysql://localhost:3306/TT?...
```

**已有 `TT` 库和演示数据时，不要再执行 `schema.sql`。** 该脚本开头有 `DROP TABLE`，会清掉现有表。

空库初始化：

```bash
mysql -u root -p < src/main/resources/db/schema.sql
```

补充表（组织、公告、配置等）在：

- `src/main/resources/db/migrate_ops.sql`
- `src/main/resources/db/migrate_settings.sql`

逻辑删除字段为 `status_cd`：`0` 在用，`1` 失效。

### 演示数据

| 说明 | 值 |
|---|---|
| 小区 | `2022081539020475` 测试小区 |
| 物业 | `10001` HC物业公司 |
| 楼栋 / 单元 | `3022081500000001` 1号楼 / `4022081500000001` 1单元 |
| 房屋 101（已入住） | `5022081500000001` |
| 房屋 102（未售） | `5022081500000002` |
| 业主李明 | `6022081500000001` |
| 账户 | `C022081500000001` |
| 物业费账单 | `B022081500000001` |

---

## 启动

### 后端

IDEA 运行 `TtApplication`，或：

```bash
cd springboot/springboot-TT
mvn -DskipTests spring-boot:run
```

端口 **8088**。改代码后若用了 devtools，会自动重启。

### 前端

```bash
cd springboot/springboot-TT-web
npm install
npm run dev
```

浏览器打开 http://localhost:5173

### 登录账号

| 用户名 | 密码 | 角色 |
|---|---|---|
| `admin` | `admin` | 管理员 |
| `18909711443` | `admin` | 同上（手机号登录） |
| `wuxw` | `admin` | 员工 |

密码存储规则：`md5(md5(明文 + "hc@java110"))`。

---

## 接口约定

- 路径：`/app/{服务名.动作}`，例如 `POST /app/login.pcUserLogin`
- 除登录外都要带：`Authorization: Bearer {token}`
- 新增用 `POST`，修改用 `PUT`，删除用 `DELETE`（JSON body 里带主键，例如 `{ "communityId": "..." }`）
- 成功：HTTP 200，且 `{"code":0,"msg":"成功","data":...}`
- 业务失败：仍是 HTTP 200，`code != 0`（参数错误 `1001`，账号密码错 `401`）
- 未登录 / token 无效：HTTP **401**，`msg` 为「登录已过期，请重新登录」
- 分页：`page`、`row`；返回 `data` 数组 + `total` / `page` / `rows`

登录示例：

```bash
curl -X POST http://localhost:8088/app/login.pcUserLogin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","passwd":"admin"}'
```

注意字段名是 **`passwd`**，不是 `password`。

---

## 功能模块

课程分工对应关系如下。

| 模块 | 页面 | 主要接口 |
|---|---|---|
| 小区 / 物业 / 楼栋 | 小区信息、物业公司、楼栋管理 | `community.*` `property.*` `floor.*` |
| 单元 / 工单 | 单元管理、报修、投诉 | `unit.*` `repair.*` `complaint.*` |
| 房屋 / 交房 / 退房 / 车位 | 房屋管理、车位管理 | `room.*` `parkingSpace.*` |
| 业主 / 家庭 / 账户 | 业主、家庭成员、业主账户 | `owner.*` `account.*` |
| 费用 | 费用项、账单、缴费记录、抄表 | `feeConfig.*` `fee.*` `meterWater.*` |

另外还有工作台、业务受理（按 `楼栋-单元-房号` 如 `1-1-101` 查房）、组织员工、车辆访客、巡检采购、公告问卷等，前端菜单里都能进。

房屋状态：`2001` 未售，`2002` 已入住。业主类型：`1001` 业主，`1002` 家庭成员。

---

## Apifox 测试

文件在 `apifox/`，按分工只覆盖上表模块（外加登录前置）。

1. Apifox → 导入 → Postman
2. 先导 `TT-物业环境.postman_environment.json`，再导集合
3. 环境选 **TT物业-本地8088**（`ttBaseUrl = http://localhost:8088`，不要用 8080）
4. 先跑「00-登录 / 登录-账号密码成功」，写入 `ttToken`
5. 带序号的闭环用例必须按顺序跑，新建 ID 由后置脚本写入环境变量

改用例后重新生成：

```bash
python3 apifox/generate_collection.py
```

---

## 常见问题

**Apifox `connect ECONNREFUSED 127.0.0.1:8080`**  
后端端口是 8088。环境变量要用 `ttBaseUrl`，不要沿用其它项目的 `baseUrl=8080`。

**登录成功但其它接口 401**  
没带 `Authorization: Bearer {token}`，或 token 过期（默认 2 小时）。

**前端能开页面但接口全失败**  
先确认 8088 已启动；Vite 只代理 `/app`。

**改了 Java 接口前端没变化**  
重启 `TtApplication`。前端改 `.vue` 一般会热更新。
