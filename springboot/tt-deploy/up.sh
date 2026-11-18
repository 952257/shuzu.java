#!/usr/bin/env sh
# TT 前后端 Docker 一键启动（自动生成密钥，拒绝空密钥/示例密钥）
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

rand_secret() {
  # 使用 hex，避免 base64 中的 +/= 干扰 Compose / mysqladmin
  if command -v openssl >/dev/null 2>&1; then
    openssl rand -hex 32
  else
    dd if=/dev/urandom bs=32 count=1 2>/dev/null | od -An -tx1 | tr -d ' \n'
  fi
}

is_bad_secret() {
  # 空值、过短、或仓库曾经出现过的示例口令
  case "$1" in
    ""|"changeme"|"CHANGE_ME"|"hjx127307"|"tt-homecommunity-secret-key-2026")
      return 0
      ;;
  esac
  # shell 下尽量避免依赖 ${#var} 之外的东西；短于 16 视为不安全
  [ "${#1}" -lt 16 ]
}

ensure_env() {
  if [ ! -f .env ]; then
    MYSQL_ROOT_PASSWORD="$(rand_secret)"
    TT_JWT_SECRET="$(rand_secret)"
    cat > .env <<EOF
# 由 ./up.sh 自动生成，请勿提交到 Git
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
TT_JWT_SECRET=${TT_JWT_SECRET}
WEB_HOST_PORT=8080
WEB_BIND_ADDRESS=0.0.0.0
BACKEND_HOST_PORT=8088
MYSQL_HOST_PORT=3309
EOF
    echo "已生成带随机密钥的 .env（不会提交到 Git）"
  fi

  # shellcheck disable=SC1091
  set -a
  # 读取 .env（忽略注释与空行）
  # 使用简单解析，避免 source 执行恶意内容
  while IFS= read -r line || [ -n "$line" ]; do
    case "$line" in
      ""|\#*) continue ;;
    esac
    key="${line%%=*}"
    val="${line#*=}"
    case "$key" in
      MYSQL_ROOT_PASSWORD|TT_JWT_SECRET|WEB_HOST_PORT|WEB_BIND_ADDRESS|BACKEND_HOST_PORT|MYSQL_HOST_PORT)
        export "$key=$val"
        ;;
    esac
  done < .env
  set +a

  if is_bad_secret "${MYSQL_ROOT_PASSWORD:-}"; then
    echo "错误：MYSQL_ROOT_PASSWORD 未设置或不安全。请编辑 .env 或删除 .env 后重跑 ./up.sh。"
    exit 1
  fi
  if is_bad_secret "${TT_JWT_SECRET:-}"; then
    echo "错误：TT_JWT_SECRET 未设置或不安全（至少 16 字符随机串）。请编辑 .env 或删除 .env 后重跑 ./up.sh。"
    exit 1
  fi
}

ensure_env

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
echo "  后端 API : http://127.0.0.1:${BACKEND_HOST_PORT:-8088}（仅本机）"
echo "  MySQL    : 127.0.0.1:${MYSQL_HOST_PORT:-3309}（仅本机）"
echo "  登录账号 : admin / admin"
echo ""
echo "验证示例："
echo "  curl -s http://localhost:${WEB_HOST_PORT:-8080}/ | head"
echo "  curl -s -X POST http://localhost:${WEB_HOST_PORT:-8080}/app/login.pcUserLogin -H 'Content-Type: application/json' -d '{\"username\":\"admin\",\"passwd\":\"admin\"}'"
