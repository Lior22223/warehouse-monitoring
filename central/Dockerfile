FROM gradle:8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle :central:clean :central:bootJar --no-daemon && ls -al central/build/libs

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/central/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

