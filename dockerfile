# Etapa 1: compilar el jar
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
RUN ls -l /app/target/

# Etapa 2: empaquetar la app
FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]
