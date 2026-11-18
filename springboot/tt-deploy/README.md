# TT 前后端 Docker Web 部署

一键发布 **前端（Vue）+ 后端（Spring Boot）+ MySQL**，通过 Nginx 在 **8080** 对外提供 Web 访问。

## 快速启动

```bash
cd springboot/tt-deploy
./up.sh
# 或：先准备安全的 .env，再 docker compose up -d --build
```

`./up.sh` 会在没有 `.env` 时生成**随机** `MYSQL_ROOT_PASSWORD` / `TT_JWT_SECRET`，并拒绝空值或已知示例口令。

| 服务 | 地址 | 暴露范围 |
|------|------|----------|
| 前端 Web（含 `/app` 反代） | http://localhost:8080 | 默认 `0.0.0.0`（可用 `WEB_BIND_ADDRESS` 改成 `127.0.0.1`） |
| 后端直连 | http://127.0.0.1:8088 | **仅本机回环** |
| MySQL | 127.0.0.1:3309 | **仅本机回环** |

登录：`admin` / `admin`

## 安全注意

- `.env` 已在 `.gitignore` 中，**不要提交**真实密码 / JWT。
- `.env.example` 不含可用密钥，只作字段说明。
- Compose **不提供**密钥默认值；缺密钥会启动失败。
- MySQL / 后端宿主机端口绑定 `127.0.0.1`，不会默认对公网开放。
- 公网只应暴露 Web 端口（或前面再加 HTTPS 反代），并在云安全组收紧来源。

## 停止

```bash
docker compose down
# 连同数据卷清空（会丢库）：docker compose down -v
```

## 常见问题

- **backend 连不上 mysql / Nginx 反代 504**：同 bridge 容器互访异常时执行  
  `sudo sysctl -w net.bridge.bridge-nf-call-iptables=0 net.bridge.bridge-nf-call-ip6tables=0`  
  然后 `docker compose restart backend`。`./up.sh` 会尝试自动设置。
- **端口冲突**：改 `.env` 里的 `WEB_HOST_PORT` / `BACKEND_HOST_PORT` / `MYSQL_HOST_PORT`。
- **改了密码但库起不来**：旧数据卷仍是旧 root 密码。需要 `docker compose down -v` 后重来（会清空演示数据）。
- **公网发布**：本机先能打开 `http://<服务器IP>:8080`，再在云厂商安全组放行该端口，或用 Nginx/Caddy 反代到域名并配置 HTTPS。
