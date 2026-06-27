## syntax=docker/dockerfile:1.7

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY docker/maven-settings.xml /tmp/maven-settings.xml

COPY starter-template/pom.xml starter-template/pom.xml
COPY starter-template/prep-errors-module/pom.xml starter-template/prep-errors-module/pom.xml
COPY starter-template/prep-errors-starter/pom.xml starter-template/prep-errors-starter/pom.xml
COPY starter-template/prep-errors-starter-test/pom.xml starter-template/prep-errors-starter-test/pom.xml
COPY prep-assessment/pom.xml prep-assessment/pom.xml
COPY prep-assessment/prep-assessment-api/pom.xml prep-assessment/prep-assessment-api/pom.xml
COPY prep-assessment/prep-assessment-service/pom.xml prep-assessment/prep-assessment-service/pom.xml
COPY prep-auth/pom.xml prep-auth/pom.xml
COPY prep-auth/prep-auth-api/pom.xml prep-auth/prep-auth-api/pom.xml
COPY prep-auth/prep-auth-common/pom.xml prep-auth/prep-auth-common/pom.xml
COPY prep-auth/prep-auth-service/pom.xml prep-auth/prep-auth-service/pom.xml
COPY prep-auth/prep-auth-starter/pom.xml prep-auth/prep-auth-starter/pom.xml
COPY prep-auth/prep-auth-starter-test/pom.xml prep-auth/prep-auth-starter-test/pom.xml

COPY starter-template starter-template
COPY prep-assessment/prep-assessment-api prep-assessment/prep-assessment-api
COPY prep-auth prep-auth

RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    mvn -q -s /tmp/maven-settings.xml -pl prep-auth/prep-auth-service -am -DskipTests package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /workspace/prep-auth/prep-auth-service/target/prep-auth-service-0.1.0-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
