Product API

A RESTful Product API built using Java 17 and Spring Boot 3.5.5.

This application provides product management, product-item relationships, user authentication, JWT-based authentication, role-based authorization, MySQL database integration, Swagger/OpenAPI documentation, and Docker support.





Technologies Used

- Java 17
- Spring Boot 3.5.5
- Spring Web
- Spring Data JPA
- Spring Security
- JWT Authentication
- MySQL 8.4
- Maven
- Swagger / OpenAPI
- Docker
- Docker Compose

Project Structure

    Product_Api/
    │
    ├── .mvn/
    │
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── com/Zest/Product_Api/
    │   │   │       ├── Config/
    │   │   │       │   └── AdminInitializer.java
    │   │   │       ├── Controller/
    │   │   │       │   ├── AuthController.java
    │   │   │       │   └── ProductController.java
    │   │   │       ├── dto/
    │   │   │       ├── Entity/
    │   │   │       │   ├── Item.java
    │   │   │       │   ├── Product.java
    │   │   │       │   ├── RefreshToken.java
    │   │   │       │   └── User.java
    │   │   │       ├── Exception/
    │   │   │       ├── Repository/
    │   │   │       │   ├── ItemRepo.java
    │   │   │       │   ├── ProductRepo.java
    │   │   │       │   ├── RefreshTokenRepo.java
    │   │   │       │   └── UserRepo.java
    │   │   │       ├── Security/
    │   │   │       ├── Service/
    │   │   │       │   ├── AuthService.java
    │   │   │       │   └── ProductService.java
    │   │   │       └── ProductApiApplication.java
    │   │   └── resources/
    │   │       └── application.properties
    │   └── test/
    │
    ├── Dockerfile
    ├── compose.yaml
    ├── pom.xml
    ├── mvnw
    ├── mvnw.cmd
    ├── README.md
    └── .gitignore

Requirements

Local Development

- Java 17
- MySQL 8.x
- Maven

The project includes the Maven Wrapper, so Maven does not need to be installed separately.

Docker

- Docker
- Docker Compose

Database Configuration

The application uses MySQL with Spring Data JPA and Hibernate.

Local Database

    Host: localhost
    Port: 3306
    Database: productdb
    Username: username
    Password: password

Create the database if it does not already exist:

SQL

CREATE DATABASE productdb;
