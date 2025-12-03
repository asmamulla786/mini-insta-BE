# Step 1 — Build stage
FROM gradle:8.4-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean bootJar --no-daemon

# Step 2 — Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
