FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=build app/target/solosync.jar solosync.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar","/solosync.jar"]
