FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/solosync.jar solosync.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "solosync.jar"]