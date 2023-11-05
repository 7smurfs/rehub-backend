FROM openjdk:17-alpine

RUN mkdir /app

COPY rehub-1.0.0.jar /app/rehub-1.0.0.jar
WORKDIR /app



LABEL authors="begjosip"

CMD ["java", "-Dspring.profiles.active=local", "-jar", "rehub-1.0.0.jar"]
