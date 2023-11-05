FROM openjdk:17-alpine

RUN mkdir /app

COPY rehub-1.0.0.jar /app/rehub-1.0.0.jar
WORKDIR /app



LABEL authors="begjosip"
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "rehub-1.0.0.jar"]
