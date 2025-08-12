FROM amazoncorretto:21-alpine-jdk
COPY target/solosync.jar solosync.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/solosync.jar"]
