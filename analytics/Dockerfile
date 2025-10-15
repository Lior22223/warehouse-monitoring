FROM gradle:8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle :analytics:clean :analytics:bootJar --no-daemon && ls -al analytics/build/libs

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/analytics/build/libs/app.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]


