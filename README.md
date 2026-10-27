# Calculator REST API & DevOps Pipeline

End-to-end CI/CD pipeline for a Spring Boot Calculator REST API: Git/GitHub → Jenkins → Docker → Docker Compose.

Developed for **CSE 816: Software Production Engineering (SPE)** under Prof. B. Thangaraju.

---

## 1. Overview

The Calculator API is a production-grade microservice implemented in Java 17 and Spring Boot 3. It exposes standard arithmetic operations (`add`, `subtract`, `multiply`, `divide`) with exact decimal precision, input validation, structured HTTP 400 error payloads, and Spring Boot Actuator health checks.

---

## 2. Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.4.2 (Spring Web, Spring Boot Actuator)
- **Build & Dependency Management:** Apache Maven with Maven Wrapper (`./mvnw`)
- **Testing:** JUnit 5, AssertJ, Spring MockMvc
- **Code Coverage:** JaCoCo (80% line coverage gate)

---

## 3. API Reference

All calculator endpoints are rooted at `/api/v1` and accept query parameters `a` and `b`.

| Operation | Method | Endpoint | Query Params | Description |
|---|---|---|---|---|
| **Add** | `GET` | `/api/v1/add` | `a`, `b` | Returns `a + b` |
| **Subtract** | `GET` | `/api/v1/subtract` | `a`, `b` | Returns `a - b` |
| **Multiply** | `GET` | `/api/v1/multiply` | `a`, `b` | Returns `a * b` |
| **Divide** | `GET` | `/api/v1/divide` | `a`, `b` | Returns `a / b` (HTTP 400 on `b=0`) |
| **Health** | `GET` | `/actuator/health` | None | Spring Boot Actuator health probe |
| **Info** | `GET` | `/actuator/info` | None | Application metadata & version |

### Example Requests & Responses

#### 1. Addition
```bash
curl -s "http://localhost:8080/api/v1/add?a=2&b=3"
```
**Response (HTTP 200):**
```json
{
  "operation": "add",
  "a": 2,
  "b": 3,
  "result": 5
}
```

#### 2. Division (Decimal Precision)
```bash
curl -s "http://localhost:8080/api/v1/divide?a=10&b=4"
```
**Response (HTTP 200):**
```json
{
  "operation": "divide",
  "a": 10,
  "b": 4,
  "result": 2.5
}
```

#### 3. Division by Zero (Validation Error)
```bash
curl -s -i "http://localhost:8080/api/v1/divide?a=10&b=0"
```
**Response (HTTP 400 Bad Request):**
```json
{
  "timestamp": "2026-10-27T11:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Division by zero is not allowed",
  "path": "/api/v1/divide"
}
```

#### 4. Missing Parameter
```bash
curl -s -i "http://localhost:8080/api/v1/add?a=1"
```
**Response (HTTP 400 Bad Request):**
```json
{
  "timestamp": "2026-10-27T11:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Missing required parameter 'b'",
  "path": "/api/v1/add"
}
```

#### 5. Health Check
```bash
curl -s "http://localhost:8080/actuator/health"
```
**Response (HTTP 200):**
```json
{
  "status": "UP"
}
```

---

## 4. Build, Test, and Run Locally

### Prerequisites
- JDK 17 or higher
- Git

### Commands

1. **Compile and run tests:**
   ```bash
   ./mvnw clean test
   ```
   Surefire reports are written to `target/surefire-reports/`, and the JaCoCo coverage report is generated at `target/site/jacoco/index.html`.

2. **Package the executable JAR:**
   ```bash
   ./mvnw clean package
   ```
   Produces `target/calculator-api.jar`.

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   # or
   java -jar target/calculator-api.jar
   ```

---

## 5. Project Structure

```text
.
├── .github/
│   └── pull_request_template.md
├── docs/
│   └── SPE_Mini_Project.docx
├── src/
│   ├── main/
│   │   ├── java/com/vaishak/calculator/
│   │   │   ├── CalculatorApplication.java
│   │   │   ├── controller/
│   │   │   │   └── CalculatorController.java
│   │   │   ├── dto/
│   │   │   │   ├── CalculationResponse.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── DivisionByZeroException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── service/
│   │   │       └── CalculatorService.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/vaishak/calculator/
│           ├── controller/
│           │   └── CalculatorControllerTest.java
│           └── service/
│               └── CalculatorServiceTest.java
├── .gitattributes
├── .gitignore
├── LICENSE
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```
