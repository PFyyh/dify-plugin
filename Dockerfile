# 使用 JDK8 作为基础镜像
FROM openjdk:8-jdk-slim

# 安装 curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 设置工作目录
WORKDIR /app

# 设置环境变量
ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    HOST_MACHINE=host.docker.internal

# 从本地构建目录复制 jar 文件
ADD ./target/demo-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]