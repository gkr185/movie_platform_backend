@echo off
echo ================================
echo 启动统计服务 (StatisticsService)
echo 端口: 8069
echo ================================

cd /d "%~dp0StatisticsService"

echo 正在启动统计服务...
mvn spring-boot:run

pause 