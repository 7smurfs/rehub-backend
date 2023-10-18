# ReHUB

This README will guide you through the setup and usage of the project.

## Table of contents

1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Getting Started](#getting-started)
4. [API Endpoints](#api-endpoints)
5. [Testing](#testing)

## Project Overview

This Spring Boot application serves as the backend for ReHUB project. It provides a foundation for building RESTful APIs, handling database interactions, and managing your application's business logic.

## Prerequisites

Before you get started, make sure you have the following prerequisites installed on your development machine:

- Java Development Kit (JDK) Version 17
- Gradle (for building the project)
- PostgreSQL database

## Getting Started

To set up this project on your local machine, follow these steps:

1. Clone the repository (if you haven't already):
    ```bash
    git clone https://github.com/7smurfs/rehub-backend.git

2. Run local postgres container for database. Docker compose file can be found in
`docker/docker-compose.yaml`. To compose postgres container run 
    ```bash
    docker-compose up -d

3. Run the application class (gradle or through IDE).
    ```bash
    ./gradlew bootRun --args='--spring.profiles.active=local'
   
## API Endpoints

List of API endpoints

## Testing

To start all test run command:
```bash
./gradlew test
