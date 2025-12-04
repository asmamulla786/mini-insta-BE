# -------- Stage 1: Build the Spring Boot app --------
FROM gradle:8.4-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN gradle build -x test --no-daemon

# -------- Stage 2: Run the Spring Boot app --------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
