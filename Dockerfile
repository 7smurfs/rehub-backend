FROM openjdk:17-alpine

WORKDIR /app

COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
COPY gradlew.bat ./

COPY src src

RUN ./gradlew build

LABEL authors="begjosip"

CMD ["java", "-Dspring.profiles.active=local", "-jar", "build/libs/rehub-1.0.0.jar"]
