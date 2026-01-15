# backend/Dockerfile
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml
COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /opt/app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
