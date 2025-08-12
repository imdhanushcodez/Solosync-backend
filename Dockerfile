# Step 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run the JAR
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/solosync.jar solosync.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "solosync.jar"]
