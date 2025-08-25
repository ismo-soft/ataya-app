# Dockerfile
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/company.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app/company.jar"]
