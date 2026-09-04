@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo [1/3] 本机创建库 blade、ry-vue （MySQL root / 123456）
mysql -h127.0.0.1 -uroot -p123456 -e "CREATE DATABASE IF NOT EXISTS blade DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci; CREATE DATABASE IF NOT EXISTS `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
if errorlevel 1 (
  echo 失败：请先在本机启动 MySQL，并确认 root 密码是 123456
  exit /b 1
)

echo [2/3] 导入 Blade 官方 SQL
mysql -h127.0.0.1 -uroot -p123456 --default-character-set=utf8mb4 blade < "blade-springboot\doc\sql\blade-saber-mysql.sql"
if errorlevel 1 (
  echo 失败：导入 blade-saber-mysql.sql 出错
  exit /b 1
)

echo [3/3] 导入 exam_demo 台账表
mysql -h127.0.0.1 -uroot -p123456 --default-character-set=utf8mb4 < "exam_demo\sql\schema.sql"
if errorlevel 1 (
  echo 失败：导入 exam_demo\sql\schema.sql 出错
  exit /b 1
)

echo.
echo 本机数据库已就绪。接下来不要 Docker、不要虚拟机：
echo   1. 本机启动 Redis 6379，密码 123456（没有密码就注释掉 blade application-dev.yml 里的 password）
echo   2. IDEA 运行 org.springblade.Application （JDK 17，端口 9999）
echo   3. IDEA 运行 com.zhrj.DemoApplication （端口 8080，前缀 /exam）
pause
