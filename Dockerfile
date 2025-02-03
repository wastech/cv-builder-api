FROM openjdk:21-jdk

WORKDIR /app

COPY target/cv-builder-api-0.0.1-SNAPSHOT.jar /app/cv-builder-api.jar

# Copy the application.properties file
COPY src/main/resources/application.properties /app/application.properties
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/cv-builder-api.jar"]