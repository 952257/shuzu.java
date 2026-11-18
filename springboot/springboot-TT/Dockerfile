# 后端：多阶段构建 Spring Boot jar，再打成精简运行镜像
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package \
    && JAR="$(ls target/*.jar | grep -v '\.original$' | head -n1)" \
    && cp "$JAR" /build/app.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN groupadd -r tt && useradd -r -g tt tt
COPY --from=build /build/app.jar /app/app.jar
USER tt
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
