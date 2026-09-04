CREATE USER IF NOT EXISTS 'repl'@'%' IDENTIFIED WITH mysql_native_password BY '127307';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
GRANT SELECT ON performance_schema.* TO 'repl'@'%';
FLUSH PRIVILEGES;
