# Etapa 1: build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -Dmaven.compiler.release=21 package

# Etapa 2: imagem final, só com o runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/mlp-site.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
