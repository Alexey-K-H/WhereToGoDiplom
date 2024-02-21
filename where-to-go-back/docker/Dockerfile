FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY ../target/wheretogo-*.jar /app/application.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "application.jar"]