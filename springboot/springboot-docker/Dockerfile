FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/springboot-docker-*.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "app.jar"]
