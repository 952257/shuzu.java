#!/usr/bin/env sh
# TT 前后端 Docker 一键启动
set -eu
cd "$(dirname "$0")"

if docker compose version >/dev/null 2>&1; then
  COMPOSE="docker compose"
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE="docker-compose"
else
  echo "需要 docker compose 或 docker-compose"
  exit 1
fi

if [ ! -f .env ]; then
  cp .env.example .env
  echo "已生成 .env（可按需修改密码与端口）"
fi

# 部分环境开启 br_netfilter 后，同 bridge 容器互访会被 iptables 干扰。
# 若出现 backend 连不上 mysql、前端反代 504，可执行：
#   sudo sysctl -w net.bridge.bridge-nf-call-iptables=0 net.bridge.bridge-nf-call-ip6tables=0
if [ "$(id -u)" = "0" ] || command -v sudo >/dev/null 2>&1; then
  if [ -r /proc/sys/net/bridge/bridge-nf-call-iptables ]; then
    sudo sysctl -w net.bridge.bridge-nf-call-iptables=0 net.bridge.bridge-nf-call-ip6tables=0 >/dev/null 2>&1 || true
  fi
fi

$COMPOSE up -d --build
echo ""
echo "已启动："
echo "  前端 Web : http://localhost:${WEB_HOST_PORT:-8080}"
echo "  后端 API : http://localhost:${BACKEND_HOST_PORT:-8088}"
echo "  登录账号 : admin / admin"
echo ""
echo "验证示例："
echo "  curl -s http://localhost:${WEB_HOST_PORT:-8080}/ | head"
echo "  curl -s -X POST http://localhost:${WEB_HOST_PORT:-8080}/app/login.pcUserLogin -H 'Content-Type: application/json' -d '{\"username\":\"admin\",\"passwd\":\"admin\"}'"
