# exam_demo（官方源码 + 企业开发业务）

考试工程来自：https://gitee.com/iocs/exam_demo.git

第三方来自：https://gitee.com/qwqweqwe/blade-springboot.git  
（已放在同级目录 `../blade-springboot`）

不要再从零新建 `state-grid-app`。业务代码全部加在本工程 `com.zhrj.exam` 包下。

## 启动顺序

1. MySQL 导入 `blade-springboot/doc/sql/blade-saber-mysql.sql`（库名 `blade`），启动 Redis。
2. 启动 `org.springblade.Application`（端口 9999）。
3. 启动 `com.zhrj.DemoApplication`（端口 8080，路径前缀 `/exam`）。

本机没有 MySQL 时，保持 `spring.profiles.active=h2`。连不上 Blade 时启动日志会 warn，Blade 起来后再 `POST /exam/job/sync`。

## 试卷接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/exam/auth/token` | 按 yml 调远程 OAuth，返回 Token |
| GET | `/exam/report/export?date=yyyy-MM-dd` | 下载 Word 日报 |
| POST | `/exam/job/sync` | 立刻增量同步（调试） |

远程地址默认 `http://127.0.0.1:9999/blade-auth/token`。Nginx 起来后可改成试卷原文的 `http://localhost/blade-auth/token`。
