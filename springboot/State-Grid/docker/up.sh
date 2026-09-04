#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")"
docker compose up -d
echo
echo "中间件已启动。常用地址："
echo "  MySQL 主  3306 / 从 3307   账号 root / 127307"
echo "  Redis     6379             密码 127307"
echo "  MinIO     http://127.0.0.1:9001   remote_user / 0123456789.abcdefg  bucket=demo"
echo "  Nginx     http://127.0.0.1/"
echo "  Prometheus http://127.0.0.1:9090"
echo "  Grafana   http://127.0.0.1:3000   admin / 127307"
echo
echo "接下来请启动 IDEA 中的 blade-mock（18080）和 state-grid-app（8080）"
