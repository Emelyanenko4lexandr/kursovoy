FROM gradle:7.6-jdk17-focal AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle build -x test

FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]