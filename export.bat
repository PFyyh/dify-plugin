@echo off
chcp 65001

echo 开始Maven Clean...
call mvn clean -f "d:\job\ai\download\pom.xml"
if %ERRORLEVEL% NEQ 0 (
    echo Maven Clean 失败！
    exit /b 1
)

echo 开始Maven Package...
call mvn package -f "d:\job\ai\download\pom.xml" -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Maven Package 失败！
    exit /b 1
)

echo 开始构建Docker镜像...
docker build -t springboot-demo:1.0.0 .
if %ERRORLEVEL% NEQ 0 (
    echo Docker构建失败！
    exit /b 1
)

echo 开始导出Docker镜像...
docker save -o springboot-demo-1.0.0.tar springboot-demo:1.0.0
if %ERRORLEVEL% NEQ 0 (
    echo 导出镜像失败！
    exit /b 1
)

echo 所有操作完成！