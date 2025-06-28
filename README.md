# AuditCenter API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen)
![Security](https://img.shields.io/badge/Security-JWT%20&%20BCrypt-critical)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-blueviolet)
![Maven](https://img.shields.io/badge/Build-Maven-orange)

**AuditCenter** is a robust and secure RESTful API built with Java and Spring Boot, designed to centralize and manage audit events from multiple systems. The application features JWT-based authentication, role-based access control, and an innovative real-time event streaming capability using Server-Sent Events (SSE).

---

## 🏛️ API Architecture

This diagram illustrates the flow of requests, security layers, and component interactions within the API.

![API Architecture Diagram](https://i.ibb.co/4g1g0gtS/Editor-Mermaid-Chart-2025-06-28-151636.png)

---

## ✨ Key Features

- **State-of-the-Art Security**: JWT authentication and BCrypt-encrypted passwords. Endpoints are protected based on user roles (`ADMIN`, `ANALYST`).
- **Audit API**:
  - `POST /events/webhook`: An endpoint to receive audit events from external systems.
  - `GET /events`: Lists audit events. An `ADMIN` can see all events, while an `ANALYST` can only see events associated with their email.
- **Real-Time Streaming**:
  - `GET /events/stream`: Establishes a Server-Sent Events (SSE) connection, allowing clients to receive audit events as they happen.
- **Authentication API**:
  - `POST /auth/register`: Registers new users (`ADMIN` or `ANALYST`).
  - `POST /auth/login`: Authenticates users and returns a JWT.
- **Interactive Documentation**: Fully documented API with Swagger (OpenAPI 3), enabling easy exploration and testing of all endpoints.
- **Error Handling**: Standardized and clear error responses for a better developer experience.

---

## 🛠️ Project Structure

The project follows a layered architecture to ensure separation of concerns and maintainability:

```
com.auditcenter
├── config         // Spring Security and Bean configurations
├── controller     // REST Controllers (API entry points)
├── dto            // Data Transfer Objects (for requests and responses)
├── entity         // JPA Entities (data model)
├── exception      // Global exception handler
├── mapper         // MapStruct mappers for DTO-Entity conversion
├── repository     // Spring Data JPA repositories (database access)
├── security       // JWT logic, filters, and UserDetailsService
└── service        // Business logic of the application
```

---

## 🚀 How to Set Up and Run the Project

### Prerequisites
- **Java 17** or higher
- **Apache Maven** 3.6 or higher
- A terminal (`cmd`, `PowerShell`, or `Git Bash`)

### Execution Steps

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/YuriTheCoder/AuditCenter-API.git
    cd AuditCenter-API
    ```

2.  **Run the Application via Maven**:
    This command will compile the project and start the embedded web server on port `8080`.
    ```bash
    mvn spring-boot:run
    ```
    The server is ready when you see the message `Tomcat started on port(s): 8080 (http)`. Keep this terminal running.

3.  **Access the API Documentation (Swagger)**:
    With the server running, open your browser and navigate to:
    [**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)

---

## ✅ Automated Tests

The project includes unit tests for the service layer using JUnit and Mockito to ensure the business logic is working correctly.

To run the tests, execute the following command in your terminal:
```bash
mvn test
```
Test classes are located in `src/test/java/com/auditcenter/service/`.

---

## 🔐 Using the API with JWT

All protected endpoints require a JWT Bearer Token for authorization.

1.  **Get the Token**: Use the `POST /auth/login` endpoint with a registered user's credentials to get an `accessToken`.
2.  **Authorize in Swagger**: Click the **Authorize** button at the top of the Swagger UI. In the dialog, paste your token in the following format:
    ```
    Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    ```
3.  **Execute**: You can now successfully call any protected endpoint!

---

## ☁️ Deploy (Optional)

You can deploy this API for free on a cloud platform like Render or Railway.

1.  Push your final code to your GitHub repository.
2.  Create a new "Web Service" in [Render](https://render.com) and connect it to your GitHub repository.
3.  Render will automatically detect it's a Java application and use a command like `mvn spring-boot:run` to start it.
4.  Your API will be deployed with a public URL, and Swagger will be available at `your-url.onrender.com/swagger-ui.html`. 