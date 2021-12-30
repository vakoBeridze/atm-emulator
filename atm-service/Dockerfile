FROM gradle:7-jdk11-alpine AS build
WORKDIR /atm-service
COPY src src
COPY build.gradle .
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim
COPY --from=build /atm-service/build/libs/atm-service.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
