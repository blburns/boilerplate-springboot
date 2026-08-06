# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle gradle
COPY app app
COPY api api
COPY web web
COPY domain domain
COPY infrastructure infrastructure

RUN chmod +x gradlew && ./gradlew :app:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine AS runtime
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

COPY --from=build /workspace/app/build/libs/boilerplate-springboot-*.jar /app/app.jar

USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
