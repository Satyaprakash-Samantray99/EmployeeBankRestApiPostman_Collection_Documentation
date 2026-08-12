# EmployeeBankRestApi - API Design Document

## 1. Overview

EmployeeBankRestApi is a Spring Boot REST application used to manage employees and bank accounts.

The application provides REST APIs for:

- Employee management
- Bank account operations
- Deposit operations
- Withdrawal operations
- Account balance
- Validation
- Exception handling
- Pagination and sorting

The application uses Microsoft SQL Server for persistent data storage.

---

## 2. Technology Stack

| Component | Technology |
|---|---|
| Backend | Spring Boot |
| Programming Language | Java 21 |
| Build Tool | Maven |
| REST API | Spring Web |
| Persistence | Spring Data JPA |
| ORM | Hibernate |
| Database | Microsoft SQL Server |
| JDBC Driver | Microsoft SQL Server JDBC Driver |
| Validation | Jakarta Validation |
| Connection Pool | HikariCP |
| Monitoring | Spring Boot Actuator |
| API Documentation | Swagger / OpenAPI |
| API Testing | Postman |
| Automated API Testing | Newman |
| Caching | Redis |

---

## 3. Base URL

The application runs locally on port `8089`.

    http://localhost:8089

The API base path is:

    /api/v1

Example:

    http://localhost:8089/api/v1/employees

---

## 4. Content Type

Requests containing a JSON body must use:

    Content-Type: application/json

Successful API responses are generally returned in JSON format.

DELETE operations return `204 No Content` and therefore do not contain a response body.

---

## 5. Employee APIs

### 5.1 Get All Employees

Returns a paginated list of employees.

**Endpoint**

    GET /api/v1/employees

**Example**

    GET http://localhost:8089/api/v1/employees

**Pagination**

The API supports pagination and sorting through Spring Data `Pageable`.

Example:

    GET /api/v1/employees?page=0&size=10

Example with sorting:

    GET /api/v1/employees?page=0&size=10&sort=name,asc

**Success Response**

**Status: 200 OK**

    {
      "content": [
        {
          "id": 1,
          "name": "Rahul Sharma",
          "email": "rahul@example.com",
          "designation": "Software Engineer",
          "salary": 50000
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 10
      }
    }

The exact pagination metadata depends on the configured Spring Data response.

### 5.2 Get Employee By ID

Returns an employee using its ID.

**Endpoint**

    GET /api/v1/employees/{id}

**Example**

    GET http://localhost:8089/api/v1/employees/1

**Success Response**

**Status: 200 OK**

    {
      "id": 1,
      "name": "Rahul Sharma",
      "email": "rahul@example.com",
      "designation": "Software Engineer",
      "salary": 50000
    }

**Error Response**

If the employee does not exist:

**Status: 404 Not Found**

    {
      "timestamp": "2026-08-12T10:30:00",
      "status": 404,
      "message": "Employee not found",
      "errors": null,
      "path": "/api/v1/employees/100"
    }

### 5.3 Create Employee

Creates a new employee.

**Endpoint**

    POST /api/v1/employees

**Request Body**

    {
      "name": "Rahul Sharma",
      "email": "rahul@example.com",
      "designation": "Software Engineer",
      "salary": 50000
    }

**Validation**

- Name must not be blank.
- Email must not be blank.
- Email must be valid.
- Designation must not be blank.
- Salary must be positive.
- Email must be unique.

**Success Response**

**Status: 201 Created**

    {
      "id": 1,
      "name": "Rahul Sharma",
      "email": "rahul@example.com",
      "designation": "Software Engineer",
      "salary": 50000
    }

**Possible Errors**

| Status | Reason |
|---|---|
| 400 Bad Request | Validation failure |
| 409 Conflict | Duplicate email |
| 500 Internal Server Error | Unexpected server error |

### 5.4 Update Employee

Updates an existing employee.

**Endpoint**

    PUT /api/v1/employees/{id}

**Example**

    PUT http://localhost:8089/api/v1/employees/1

**Request Body**

    {
      "name": "Rahul Sharma",
      "email": "rahul@example.com",
      "designation": "Senior Software Engineer",
      "salary": 65000
    }

**Success Response**

**Status: 200 OK**

    {
      "id": 1,
      "name": "Rahul Sharma",
      "email": "rahul@example.com",
      "designation": "Senior Software Engineer",
      "salary": 65000
    }

**Error Responses**

- `404 Not Found` - Employee does not exist.
- `400 Bad Request` - Request validation fails.
- `409 Conflict` - Updated email already exists.

### 5.5 Delete Employee

Deletes an existing employee.

**Endpoint**

    DELETE /api/v1/employees/{id}

**Example**

    DELETE http://localhost:8089/api/v1/employees/1

**Success Response**

**Status: 204 No Content**

The response does not contain a body.

**Error Response**

If the employee does not exist:

**Status: 404 Not Found**

---

## 6. Account APIs

### 6.1 Deposit Money

Deposits money into an existing account.

**Endpoint**

    POST /api/v1/accounts/{id}/deposit

**Example**

    POST http://localhost:8089/api/v1/accounts/1/deposit

**Request Body**

    {
      "amount": 1000
    }

**Validation**

- Amount must be provided.
- Amount must be positive.
- Amount must satisfy the configured business rules.

**Success Response**

**Status: 200 OK**

The account balance is updated after a successful deposit.

**Error Responses**

| Status | Reason |
|---|---|
| 400 Bad Request | Invalid deposit amount |
| 404 Not Found | Account does not exist |
| 422 Unprocessable Entity | Business rule violation |

### 6.2 Withdraw Money

Withdraws money from an existing account.

**Endpoint**

    POST /api/v1/accounts/{id}/withdraw

**Example**

    POST http://localhost:8089/api/v1/accounts/1/withdraw

**Request Body**

    {
      "amount": 500
    }

**Processing**

The service checks:

- Whether the account exists.
- Whether the withdrawal amount is valid.
- Whether sufficient balance is available.
- Whether the transaction satisfies the configured business rules.

**Success Response**

**Status: 200 OK**

**Insufficient Balance**

If the requested withdrawal amount is greater than the available balance:

**Status: 400 Bad Request**

Example message:

    Insufficient balance.

### 6.3 Get Account Balance

Returns the current balance of an account.

**Endpoint**

    GET /api/v1/accounts/{id}/balance

**Example**

    GET http://localhost:8089/api/v1/accounts/1/balance

**Success Response**

**Status: 200 OK**

The response contains the current account balance.

**Error Response**

If the account does not exist:

**Status: 404 Not Found**

---

## 7. Employee Validation

Employee requests use Jakarta Validation.

Common validation annotations include:

    @NotBlank
    @NotNull
    @Email
    @Positive

**Validation Rules**

| Field | Validation |
|---|---|
| name | Must not be blank |
| email | Must be valid and unique |
| designation | Must not be blank |
| salary | Must be positive |

**Invalid Request Example**

    {
      "name": "",
      "email": "invalid-email",
      "designation": "",
      "salary": -100
    }

This request should return:

    400 Bad Request

---

## 8. Account Validation

Account transaction requests validate the transaction amount.

Example:

    {
      "amount": 1000
    }

The amount must satisfy the configured business rules.

An invalid amount may result in:

    400 Bad Request

---

## 9. Duplicate Employee Email

Employee email addresses are treated as unique.

Example:

    rahul@example.com

If another employee already uses the same email, the application returns:

    409 Conflict

Example:

    {
      "status": 409,
      "message": "Duplicate email"
    }

---

## 10. Global Exception Handling

The application uses centralized exception handling through:

    @RestControllerAdvice

The global exception handler provides consistent error responses across the application.

Handled exceptions include:

- `ResourceNotFoundException`
- `MethodArgumentNotValidException`
- `DuplicateEmailException`
- `BusinessException`
- Generic `Exception`

---

## 11. Error Response

The application uses a common error response structure.

Typical fields include:

- `timestamp`
- `status`
- `message`
- `errors`
- `path`

**Example**

    {
      "timestamp": "2026-08-12T10:30:00",
      "status": 404,
      "message": "Employee not found",
      "errors": null,
      "path": "/api/v1/employees/100"
    }

---

## 12. HTTP Status Codes

| Status | Meaning | Usage |
|---|---|---|
| 200 | OK | Successful GET, PUT, deposit, withdrawal and balance operations |
| 201 | Created | Successful employee creation |
| 204 | No Content | Successful employee deletion |
| 400 | Bad Request | Validation failure or business request failure |
| 404 | Not Found | Employee or account does not exist |
| 409 | Conflict | Duplicate employee email |
| 422 | Unprocessable Entity | Business rule violation where configured |
| 500 | Internal Server Error | Unexpected server-side error |

---

## 13. Request Processing Flow

The application follows a layered architecture.

    Client / Postman
           |
           v
    REST Controller
           |
           v
    Service Layer
           |
           v
    Repository Layer
           |
           v
    Spring Data JPA
           |
           v
    Hibernate
           |
           v
    HikariCP
           |
           v
    SQL Server

The controller receives the request.

The service layer performs business logic.

The repository layer communicates with the database through Spring Data JPA and Hibernate.

HikariCP manages database connections.

---

## 14. Database

The application uses:

    Microsoft SQL Server

The datasource configuration is maintained in:

    src/main/resources/application.yml

The application uses the Microsoft SQL Server JDBC driver.

Example configuration:

    spring:
      datasource:
        url: jdbc:sqlserver://SERVER\SQLEXPRESS;databaseName=shopDb;integratedSecurity=true;encrypt=true;trustServerCertificate=true
        driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver

The exact JDBC URL depends on the SQL Server installation.

---

## 15. HikariCP Configuration

The application uses HikariCP for database connection pooling.

Example configuration:

    spring:
      datasource:
        hikari:
          pool-name: ShopDevHikariPool
          maximum-pool-size: 10
          minimum-idle: 5
          connection-timeout: 30000
          max-lifetime: 1800000

**Important Properties**

| Property | Purpose |
|---|---|
| pool-name | Identifies the connection pool |
| maximum-pool-size | Maximum database connections |
| minimum-idle | Minimum idle connections |
| connection-timeout | Maximum wait time for a connection |
| max-lifetime | Maximum lifetime of a connection |

---

## 16. JPA and Hibernate

The application uses Spring Data JPA with Hibernate.

Example configuration:

    spring:
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: false

Hibernate maps Java entity classes to database tables.

The repository layer uses Spring Data JPA repositories to perform database operations.

---

## 17. Spring Boot Actuator

Spring Boot Actuator provides application monitoring endpoints.

Common endpoints include:

    /actuator/health
    /actuator/info
    /actuator/metrics

### Health

    GET /actuator/health

Example:

    http://localhost:8089/actuator/health

Expected response:

    {
      "status": "UP"
    }

### Metrics

    GET /actuator/metrics

Metrics can include:

- JVM memory
- JVM threads
- HTTP server requests
- HikariCP connections
- Disk usage
- Application startup time

---

## 18. Swagger / OpenAPI

Swagger UI provides interactive API documentation.

Example:

    http://localhost:8089/swagger-ui.html

The OpenAPI documentation endpoint is:

    http://localhost:8089/api-docs

Swagger can be used to test:

- Employee APIs
- Account APIs
- Deposit
- Withdrawal
- Account balance

---

## 19. Postman Testing

The APIs can be tested using Postman.

The recommended environment variable is:

    baseUrl = http://localhost:8089

Requests can then use:

    {{baseUrl}}/api/v1/employees

---

## 20. Recommended Postman Test Flow

The recommended testing order is:

    Start Application
           |
           v
    Verify baseUrl
           |
           v
    Get Employees
           |
           v
    Create Employee
           |
           v
    Get Employee
           |
           v
    Update Employee
           |
           v
    Create / Verify Account
           |
           v
    Deposit
           |
           v
    Get Balance
           |
           v
    Withdraw
           |
           v
    Get Balance
           |
           v
    Delete Employee

---

## 21. Newman Testing

Newman can be used to execute the Postman collection from the command line.

Verify Newman:

    newman --version

Run the collection:

    newman run "EmployeeBankRestApiPostmanCollection.postman_collection.json" -e "EmployeeBankRestApi.postman_environment.json"

If the environment does not contain `baseUrl`, provide it explicitly:

    newman run "EmployeeBankRestApiPostmanCollection.postman_collection.json" -e "EmployeeBankRestApi.postman_environment.json" --env-var "baseUrl=http://localhost:8089"

A successful run should report zero failed requests and assertions.

---

## 22. API Smoke Test

The minimum API smoke test should verify:

| API | Expected Result |
|---|---|
| Get Employees | 200 OK |
| Get Employee By ID | 200 OK or 404 Not Found |
| Create Employee | 201 Created |
| Update Employee | 200 OK |
| Delete Employee | 204 No Content |
| Deposit | 200 OK |
| Withdraw | 200 OK or 400 Bad Request |
| Get Balance | 200 OK or 404 Not Found |

---

## 23. Common API Errors

### 23.1 400 Bad Request

Possible causes:

- Invalid request body
- Missing required field
- Invalid email
- Invalid salary
- Invalid account amount
- Insufficient account balance
- Business rule violation

### 23.2 404 Not Found

Possible causes:

- Employee ID does not exist.
- Account ID does not exist.
- Incorrect endpoint URL.

### 23.3 409 Conflict

Usually caused by a duplicate employee email.

### 23.4 422 Unprocessable Entity

Used when the request is valid but violates a configured business rule.

### 23.5 500 Internal Server Error

Indicates an unexpected server-side problem.

Check the Spring Boot console and application logs for the root cause.

---

## 24. Account Transaction Flow

### Deposit

    Client
      |
      v
    Deposit Request
      |
      v
    Account Controller
      |
      v
    Account Service
      |
      v
    Validate Amount
      |
      v
    Find Account
      |
      v
    Update Balance
      |
      v
    Save Account
      |
      v
    Return Response

### Withdrawal

    Client
      |
      v
    Withdrawal Request
      |
      v
    Account Controller
      |
      v
    Account Service
      |
      v
    Validate Amount
      |
      v
    Find Account
      |
      v
    Check Balance
      |
      +---- Insufficient ----> 400 Bad Request
      |
      v
    Update Balance
      |
      v
    Save Account
      |
      v
    Return Response

---

## 25. Security and Sensitive Information

Do not expose sensitive information in API responses or logs.

Do not commit the following information to GitHub:

- Database passwords
- API keys
- Authentication tokens
- Redis passwords
- Production credentials
- Private connection strings containing secrets

Use environment-specific configuration or environment variables for sensitive values.

---

## 26. API Testing Checklist

Before considering the API implementation complete, verify:

- [ ] Employee GET API works.
- [ ] Employee GET by ID works.
- [ ] Employee POST API works.
- [ ] Employee PUT API works.
- [ ] Employee DELETE API works.
- [ ] Employee validation works.
- [ ] Duplicate email handling works.
- [ ] Account deposit works.
- [ ] Account withdrawal works.
- [ ] Insufficient balance is rejected.
- [ ] Account balance API works.
- [ ] 404 handling works.
- [ ] 400 handling works.
- [ ] 409 handling works.
- [ ] 422 handling works where configured.
- [ ] Global exception handling works.
- [ ] Actuator health works.
- [ ] Swagger UI works.
- [ ] Postman tests pass.
- [ ] Newman tests pass.

---

## 27. Related Documentation

Additional project documentation is available under the `docs` directory.

    docs/
    ├── API_DESIGN.md
    ├── ARCHITECTURE.md
    ├── DATABASE_SCHEMA.md
    ├── ENVIRONMENT_SETUP.md
    ├── RUNBOOK.md
    ├── DEPLOYMENT.md
    ├── TROUBLESHOOTING.md
    ├── FAQ.md
    └── JIRA_LINKS.md

---

## 28. API Design Summary

The EmployeeBankRestApi provides:

    Employee Management
            |
            +---- Get Employees
            +---- Get Employee
            +---- Create Employee
            +---- Update Employee
            +---- Delete Employee
            |
            v
    Account Management
            |
            +---- Deposit
            +---- Withdraw
            +---- Get Balance
            |
            v
    Validation
            |
            v
    Exception Handling
            |
            v
    SQL Server
            |
            v
    Monitoring
            |
            +---- Actuator
            +---- HikariCP
            +---- Application Metrics

---

## 29. Documentation Maintenance

This document should be updated whenever API behavior changes.

Documentation updates should be made when:

- A new endpoint is added.
- An existing endpoint changes.
- Request or response structures change.
- Validation rules change.
- HTTP status codes change.
- Exception handling changes.
- Database-related API behavior changes.
- Authentication or security changes.
- Postman tests change.

Relevant Jira ticket IDs should be recorded in:

    docs/JIRA_LINKS.md

Significant changes should also be recorded in:

    CHANGELOG.md

---

## 30. API Design Complete

The EmployeeBankRestApi API documentation should be used as the reference for:

- API development
- API testing
- Postman testing
- Newman testing
- Troubleshooting
- Code reviews
- Jira traceability
- Deployment verification