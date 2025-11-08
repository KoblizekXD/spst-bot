# ===== BUILD STAGE =====
FROM gradle:9-jdk24 AS build
WORKDIR /app

COPY gradlew .
COPY gradle /app/gradle

COPY . .

RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:24-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
