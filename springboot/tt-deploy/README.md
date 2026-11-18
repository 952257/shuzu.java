# TT 前后端 Docker Web 部署

一键发布 **前端（Vue）+ 后端（Spring Boot）+ MySQL**，通过 Nginx 在 **8080** 对外提供 Web 访问。

## 快速启动

```bash
cd springboot/tt-deploy
cp .env.example .env   # 首次
./up.sh
# 或：docker compose up -d --build
```

| 服务 | 地址 |
|------|------|
| 前端 Web（含 `/app` 反代） | http://localhost:8080 |
| 后端直连 | http://localhost:8088 |
| MySQL | localhost:3309 |

登录：`admin` / `admin`

## 停止

```bash
docker compose down
# 连同数据卷清空：docker compose down -v
```

## 常见问题

- **backend 连不上 mysql / Nginx 反代 504**：同 bridge 容器互访异常时执行  
  `sudo sysctl -w net.bridge.bridge-nf-call-iptables=0 net.bridge.bridge-nf-call-ip6tables=0`  
  然后 `docker compose restart backend`。`./up.sh` 会尝试自动设置。
- **端口冲突**：改 `.env` 里的 `WEB_HOST_PORT` / `BACKEND_HOST_PORT` / `MYSQL_HOST_PORT`。
- **公网发布**：本机先能打开 `http://<服务器IP>:8080`，再在云厂商安全组放行该端口，或用 Nginx/Caddy 反代到域名并配置 HTTPS。