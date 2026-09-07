#!/bin/sh
set -e
echo "wait slave..."
i=0
while [ "$i" -lt 30 ]; do
  if docker exec sg-mysql-slave mysqladmin ping -uroot -phjx127307 --silent >/dev/null 2>&1; then
    break
  fi
  i=$((i + 1))
  sleep 2
done

docker exec sg-mysql-slave mysql -uroot -phjx127307 -e "
STOP REPLICA;
CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='mysql-master',
  SOURCE_USER='repl',
  SOURCE_PASSWORD='hjx127307',
  SOURCE_AUTO_POSITION=1,
  GET_SOURCE_PUBLIC_KEY=1;
START REPLICA;
SHOW REPLICA STATUS\G
"
