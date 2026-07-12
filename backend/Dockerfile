# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/telehealth-pro.jar app.jar

# Most PaaS platforms (Render, Railway, etc.) inject a PORT env var and
# expect the app to listen on it. application.properties already reads
# ${PORT:8080}, so no extra config is needed here.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
