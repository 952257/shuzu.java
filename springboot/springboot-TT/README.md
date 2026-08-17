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

仓库根目录是 `javafirst`，本项目在 `springboot/` 下分后端和前端两份代码。

```
springboot/
├── springboot-TT/                     后端（Spring Boot 2.6.2，端口 8088）
└── springboot-TT-web/                 前端（Vue 3 + Vite，端口 5173）
```

### 后端 `springboot-TT/`

```
springboot-TT/
├── pom.xml
├── README.md
├── apifox/                                          接口用例（导入 Apifox）
│   ├── TT-物业接口.postman_collection.json
│   ├── TT-物业环境.postman_environment.json         环境变量 ttBaseUrl=8088
│   └── generate_collection.py                       改用例后重新生成集合
└── src/main/
    ├── java/com/tt/
    │   ├── TtApplication.java                       启动类
    │   ├── common/                                  横切能力
    │   │   ├── Result.java                          统一返回 {code,msg,data}
    │   │   ├── PageResult.java                      分页 total / page / rows
    │   │   ├── JwtUtil.java                         签发与校验 Bearer token
    │   │   ├── PasswordUtil.java                    md5(md5(passwd + hc@java110))
    │   │   ├── IdGenerator.java                     主键生成
    │   │   ├── QueryHelper.java                     列表查询条件
    │   │   ├── UserContext.java                     当前登录用户
    │   │   ├── ServiceException.java
    │   │   ├── ServiceExceptionEnum.java
    │   │   └── GlobalExceptionHandler.java
    │   ├── config/
    │   │   ├── MybatisPlusConfig.java               分页插件、逻辑删除
    │   │   └── WebMvcConfig.java                    拦截器、CORS
    │   ├── interceptor/
    │   │   └── AuthInterceptor.java                 除登录外校验 Authorization
    │   ├── dto/
    │   │   ├── LoginDto.java                        登录入参（passwd）
    │   │   └── LoginVo.java                         登录出参（token + 用户）
    │   ├── po/                                      实体，对应 MySQL 表
    │   ├── mapper/                                  MyBatis-Plus Mapper
    │   ├── service/                                 业务
    │   │   ├── LoginService.java
    │   │   ├── CommunityService.java / PropertyService.java / FloorService.java
    │   │   ├── UnitService.java / RoomService.java
    │   │   ├── OwnerService.java / OwnerAppUserService.java / AccountService.java
    │   │   ├── ParkingSpaceService.java / OwnerCarService.java
    │   │   ├── FeeConfigService.java / FeeService.java / MeterWaterService.java
    │   │   ├── RepairService.java / ComplaintService.java
    │   │   ├── StaffService.java
    │   │   ├── BizDeskService.java                  业务受理（按 1-1-101 查房）
    │   │   └── OpsService.java                      组织/公告/投票/访客/巡检/采购/合同/折扣
    │   └── controller/                              路径 /app/{服务名.动作}
    │       ├── LoginController.java                 login.pcUserLogin
    │       ├── CommunityController.java             community.*
    │       ├── PropertyController.java              property.*
    │       ├── FloorController.java                 floor.*
    │       ├── UnitController.java                  unit.*
    │       ├── RoomController.java                  room.*（含交房/退房）
    │       ├── OwnerController.java                 owner.*
    │       ├── OwnerAppUserController.java          房屋认证
    │       ├── AccountController.java               account.*
    │       ├── ParkingSpaceController.java          parkingSpace.*
    │       ├── OwnerCarController.java              ownerCar.*
    │       ├── FeeController.java                   feeConfig.* / fee.*
    │       ├── MeterWaterController.java            meterWater.*
    │       ├── RepairController.java                repair.*
    │       ├── ComplaintController.java             complaint.*
    │       ├── StaffController.java                 staff.*
    │       ├── DashboardController.java             工作台统计
    │       ├── BizDeskController.java               业务受理
    │       └── OpsController.java                   办公/巡检/采购等
    └── resources/
        ├── application.yml                          激活 dev
        ├── application-dev.yml                      端口 8088、数据源、JWT
        └── db/
            ├── schema.sql                           建表 + 基础演示数据（含 DROP，慎用）
            ├── seed_demo.sql                        扩充演示数据（INSERT IGNORE）
            ├── migrate_ops.sql                      已有库补组织/公告等表
            └── migrate_settings.sql                 已有库补小区配置表
```

`po/`、`mapper/` 与表一一对应，主要表：`u_user`、`s_store`、`community`、`f_floor`、`building_unit`、`building_room`、`building_owner`、`parking_space`、`owner_car`、`pay_fee_config`、`pay_fee`、`pay_fee_detail`、`meter_water`、`r_repair_pool`、`complaint`、`account`，以及 `tt_*` 扩展表（组织、公告、投票、访客、巡检、采购、合同、折扣、配置）。

### 前端 `springboot-TT-web/`

```
springboot-TT-web/
├── package.json
├── vite.config.js                                   /app 代理到 8088
├── index.html
└── src/
    ├── main.js                                      挂载 Vue、Element Plus、路由
    ├── App.vue                                      液态玻璃背景壳
    ├── style.css                                    全局样式、玻璃材质
    ├── api/
    │   └── http.js                                  Axios，自动带 Bearer token
    ├── utils/
    │   ├── community.js                             当前小区 communityId
    │   └── dict.js                                  状态下拉字典
    ├── router/
    │   └── index.js                                 路由表；无 token 跳登录
    ├── layout/
    │   ├── MainLayout.vue                           顶栏、图标栏、子菜单、页签
    │   └── menus.js                                 左侧菜单（小区/房产/费用/停车/报修…）
    ├── components/
    │   └── CrudPage.vue                             通用增删改查页（POST/PUT/DELETE）
    └── views/
        ├── Login.vue                                登录
        ├── Home.vue                                 工作台
        ├── BusinessDesk.vue                         业务受理
        ├── Screen.vue                               小区大屏
        ├── Settings.vue                             小区配置
        ├── Password.vue                             修改密码
        ├── Logs.vue                                 操作日志
        ├── Community.vue / Property.vue / Org.vue / Staff.vue
        ├── Floor.vue / Unit.vue / Room.vue / Shop.vue
        ├── Owner.vue / Member.vue / Auth.vue / Account.vue
        ├── Parking.vue / Car.vue / Visit.vue
        ├── FeeConfig.vue / Fee.vue / Arrears.vue
        ├── FeeDetail.vue / FeeAudit.vue / Receipt.vue
        ├── Meter.vue / Discount.vue / FeeReport.vue
        ├── Repair.vue / Complaint.vue / Inspection.vue
        ├── Purchase.vue / Contract.vue / WorkReport.vue
        └── Notice.vue / Vote.vue
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
mysql -u root -p TT < src/main/resources/db/seed_demo.sql
```

已有库只补演示数据（不会删表，主键已存在则跳过）：

```bash
mysql -u root -p TT < src/main/resources/db/seed_demo.sql
```

补充表（组织、公告、配置等）在：

- `src/main/resources/db/migrate_ops.sql`
- `src/main/resources/db/migrate_settings.sql`

逻辑删除字段为 `status_cd`：`0` 在用，`1` 失效。

### 演示数据

| 说明 | 值 |
|---|---|
| 小区 | `2022081539020475` 测试小区；`2022081539020476` 阳光花园 |
| 物业 | `10001` HC物业公司 |
| 登录 | `admin` / `admin`，员工 `wangong`、`zhaokefu` 密码均为 `admin` |
| 楼栋 | 测试小区 1/2/3 号楼；阳光花园 1 号楼 |
| 房屋 101（已入住） | `5022081500000001` |
| 业主李明 | `6022081500000001` |
| 账户 | `C022081500000001` |
| 物业费账单 | `B022081500000001` |

更全的演示数据在 `src/main/resources/db/seed_demo.sql`：约 30 套房、20 位业主、欠费账单、报修投诉、抄表、车位车辆等。

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
