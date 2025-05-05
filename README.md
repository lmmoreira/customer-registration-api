# Java 21 Application – Auth & Customer Service

## 🧾 Overview

Layered arch using **Java 21** and **Spring Boot**. It includes a simple customer domain and an authentication flow, with support for external API calls using reactive programming (`WebClient`).

---

## 📦 Features

### Authentication
- Login using email and password

### Customer Domain
- Create user
- Edit user
- List users
- Delete user
- Get user by ID
- Get user by email
- Enrich user with ZIP code information from an external service

---

## 📬 Default user: Login Request

```bash
curl --location 'http://127.0.0.1:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "johndoe@example.com",
  "password": "password"
}'
```

---

## 📬 Collections

Collections are present on the following path: 

📁 [collection](Misc.postman_collection.json)

Fell free to import them into your Postman or any other API testing tool.

---

## 🔧 Running the Application

### Run the application
```bash
mvn clean spring-boot:run
```

### Build the project
```bash
mvn clean install
```

### Run tests
```bash
mvn clean test
```

---

## 🧪 API Testing

### Swagger UI

Test the endpoints via Swagger:

[http://127.0.0.1:8080/swagger-ui/index.html](http://127.0.0.1:8080/swagger-ui/index.html)

### Postman Collection

All collections are available in the following file:

📁 [Misc.postman_collection.json](../../../Downloads/Misc.postman_collection.json)

---

## 🧱 Architecture

Layered Arch

### Layered Design

- **domain**: Entities
- **service**: Services
- **controllers**: Rest Controllers

---

## ❌ Error Handling

The API error responses follow the specification outlined in [RFC 9457](https://datatracker.ietf.org/doc/html/rfc9457).

---

## 🌐 External API Requests

The system uses **Spring WebFlux’s WebClient** to call external services in a non-blocking, reactive manner.

### ZIP Code Enrichment Example

```http
GET http://api.zippopotam.us/us/98121
```

### Resilience Configuration

- Retries: 3 attempts
- Timeout: 5 seconds

---