# Step 1: Build the JAR
FROM amazoncorretto:21-alpine-jdk
COPY target/dockermysql.jar dockermysql.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/dockermysql.jar"]
