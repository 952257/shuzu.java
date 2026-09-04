# exam_demo（官方源码 + 企业开发业务）

考试工程来自：https://gitee.com/iocs/exam_demo.git

第三方来自：https://gitee.com/qwqweqwe/blade-springboot.git  
（已放在同级目录 `../blade-springboot`）

开发在**本机 IDEA** 完成：本机 MySQL + 本机 Redis + 两个启动类。不要 Docker，不要虚拟机。运维试卷那套 compose 考试时再用。

## 本机启动

1. 本机 MySQL（`127.0.0.1:3306`，`root` / `123456`）执行一次：

```bat
cd springboot
init-local-db.bat
```

Mac / Linux：`bash init-local-db.sh`  
也可以用 Navicat 手动建库 `blade`、`ry-vue`，再分别执行 `blade-springboot/doc/sql/blade-saber-mysql.sql` 和 `exam_demo/sql/schema.sql`。

2. 本机启动 Redis `127.0.0.1:6379`，密码 `123456`。没有密码就删掉 `blade-springboot/src/main/resources/application-dev.yml` 里的 `password`。

3. IDEA 打开 `blade-springboot`，SDK 选 JDK 17，运行 `org.springblade.Application`（端口 9999）。

4. IDEA 打开 `exam_demo`，运行 `com.zhrj.DemoApplication`（端口 8080，前缀 `/exam`）。yml 已是 `spring.profiles.active: druid`，连本机 `ry-vue`。

没有 MySQL 时，把 `application.yml` 改成 `h2`，只跑 exam_demo 也可以，但拿不到第三方用户。

## 试卷接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/exam/auth/token` | 调本机 Blade OAuth，返回 Token |
| GET | `/exam/report/export?date=yyyy-MM-dd` | 下载 Word 日报 |
| POST | `/exam/job/sync` | 立刻增量同步（调试） |

第三方地址是本机 `http://127.0.0.1:9999/...`。前面再加 Nginx 时改成试卷原文 `http://localhost/blade-auth/token`。
