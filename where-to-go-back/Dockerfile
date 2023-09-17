FROM openjdk:17-jdk-slim-buster
COPY target/wheretogo-*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]