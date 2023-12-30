FROM openjdk:17-alpine

RUN mkdir /app

COPY rehub-1.0.0.jar /app/rehub-1.0.0.jar
WORKDIR /app

EXPOSE 8080
EXPOSE 587

LABEL authors="begjosip"
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "rehub-1.0.0.jar"]
