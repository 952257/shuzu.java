#!/bin/bash
set -euo pipefail

echo "[repl] waiting for mysql-master / mysql-slave ..."
for i in $(seq 1 60); do
  if mysqladmin ping -h mysql-master -uroot -p127307 --silent \
     && mysqladmin ping -h mysql-slave -uroot -p127307 --silent; then
    break
  fi
  sleep 2
done

IO_RUNNING=$(mysql -h mysql-slave -uroot -p127307 -Nse "SHOW REPLICA STATUS\G" 2>/dev/null | awk '/Replica_IO_Running:/{print $2}' || true)
if [ "${IO_RUNNING}" = "Yes" ]; then
  echo "[repl] already running"
  exit 0
fi

mysql -h mysql-slave -uroot -p127307 -e "
STOP REPLICA;
CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='mysql-master',
  SOURCE_PORT=3306,
  SOURCE_USER='repl',
  SOURCE_PASSWORD='127307',
  SOURCE_AUTO_POSITION=1,
  GET_SOURCE_PUBLIC_KEY=1;
START REPLICA;
"

echo "[repl] SHOW REPLICA STATUS"
mysql -h mysql-slave -uroot -p127307 -e "SHOW REPLICA STATUS\G" | grep -E 'Replica_|Source_Host|Last_Error|Seconds_Behind'
