FROM gradle:8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle :warehouse:clean :warehouse:bootJar --no-daemon && ls -al warehouse/build/libs

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/warehouse/build/libs/app.jar app.jar
EXPOSE 9000 3344/udp 3355/udp
ENTRYPOINT ["java", "-jar", "app.jar"]
