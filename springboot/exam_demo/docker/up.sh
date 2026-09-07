#!/bin/sh
# 企业运维一键启动。本机 MinIO 已占用 9000 时不要带 minio。
cd "$(dirname "$0")"
if docker compose version >/dev/null 2>&1; then
  COMPOSE="docker compose"
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE="docker-compose"
else
  echo "需要 docker compose 或 docker-compose"
  exit 1
fi

$COMPOSE up -d mysql-master mysql-slave redis-master redis-replica redis-sentinel nginx prometheus grafana
sh mysql/setup-replication.sh
echo "compose 已启动。主库 3307，从库 3308，Nginx 80，Prometheus 9090，Grafana 3000"
