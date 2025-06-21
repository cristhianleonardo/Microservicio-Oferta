# Etapa 1: compilar el jar
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
COPY .env .env
COPY .env /app/.env
RUN mvn clean package -DskipTests
COPY .env /app/.env

# Etapa 2: empaquetar la app
FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]
