Employee Banking REST API - Frequently Asked Questions
1. What is Employee Banking REST API?

Employee Banking REST API is a Spring Boot REST application used to manage employees and bank accounts.

The application provides APIs for:

Employee management
Account management
Deposit operations
Withdrawal operations
Account balance
Validation
Exception handling

The application uses Microsoft SQL Server for persistent data storage.

2. What technologies are used in the project?
Component	Technology
Backend	Spring Boot
Programming Language	Java 21
Build Tool	Maven
REST API	Spring Web
Persistence	Spring Data JPA
ORM	Hibernate
Database	Microsoft SQL Server
JDBC Driver	Microsoft SQL Server JDBC Driver
Validation	Jakarta Validation
Connection Pool	HikariCP
Monitoring	Spring Boot Actuator
API Testing	Postman
3. What is the application URL?

The application runs on port:

8089

The local base URL is:

http://localhost:8089

The API base path is:

/api/v1

For example:

http://localhost:8089/api/v1/employees
4. What Employee APIs are available?

The Employee APIs are:

GET    /api/v1/employees
GET    /api/v1/employees/{id}
POST   /api/v1/employees
PUT    /api/v1/employees/{id}
DELETE /api/v1/employees/{id}

These endpoints provide CRUD operations for employees.

5. What Account APIs are available?

The Account APIs are:

POST /api/v1/accounts/{id}/deposit
POST /api/v1/accounts/{id}/withdraw
GET  /api/v1/accounts/{id}/balance

These APIs are used to perform banking operations on accounts.

6. How do I create an employee?

Use:

POST /api/v1/employees

Example request:

{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "designation": "Software Engineer",
  "salary": 50000
}

A successful employee creation returns:

201 Created
7. How do I get an employee by ID?

Use:

GET /api/v1/employees/{id}

Example:

GET /api/v1/employees/1

If the employee exists:

200 OK

If the employee does not exist:

404 Not Found
8. How do I update or delete an employee?
Update
PUT /api/v1/employees/{id}

Example:

PUT /api/v1/employees/1

Successful update:

200 OK
Delete
DELETE /api/v1/employees/{id}

Successful deletion:

204 No Content
9. How do I deposit money into an account?

Use:

POST /api/v1/accounts/{id}/deposit

Example:

POST /api/v1/accounts/1/deposit

The application validates the deposit amount and updates the account balance.

A successful deposit returns:

200 OK
10. How do I withdraw money from an account?

Use:

POST /api/v1/accounts/{id}/withdraw

Example:

POST /api/v1/accounts/1/withdraw

The service checks the account balance before completing the withdrawal.

If sufficient balance is available:

200 OK

If the balance is insufficient:

400 Bad Request
11. How do I check the account balance?

Use:

GET /api/v1/accounts/{id}/balance

Example:

GET /api/v1/accounts/1/balance

If the account exists, the API returns:

200 OK

If the account does not exist:

404 Not Found
12. Why am I getting "Insufficient balance"?

The withdrawal operation checks whether the requested amount is available in the account.

For example:

Account Balance = 5000
Withdrawal      = 7000

Since the withdrawal amount is greater than the available balance, the transaction is rejected.

The API returns:

400 Bad Request

with a message similar to:

Insufficient balance.
13. What validation is implemented?

The application uses Jakarta Validation for request validation.

Common validation annotations include:

@NotBlank
@NotNull
@Email
@Positive

Examples of validation rules:

Employee name cannot be blank.
Email must be valid.
Required fields cannot be null.
Salary must be positive.
Account amounts must satisfy the configured business rules.
14. What HTTP status codes are used?
Status	Meaning	Usage
200	OK	Successful GET, PUT and banking operations
201	Created	Successful employee creation
204	No Content	Successful employee deletion
400	Bad Request	Validation or business rule failure
404	Not Found	Employee or account does not exist
409	Conflict	Duplicate resource such as email
422	Unprocessable Entity	Business rule violation where configured
500	Internal Server Error	Unexpected application error
15. Does the application use Global Exception Handling?

Yes.

The application uses:

@RestControllerAdvice

for centralized exception handling.

The global exception handler handles exceptions such as:

ResourceNotFoundException
MethodArgumentNotValidException
DuplicateEmailException
BusinessException
Exception

This ensures that APIs return consistent error responses.

16. What information is included in the error response?

The application uses an error response containing fields such as:

timestamp
status
message
errors
path

Example:

{
  "timestamp": "2026-08-12T10:30:00",
  "status": 404,
  "message": "Employee not found",
  "errors": null,
  "path": "/api/v1/employees/100"
}
17. Why am I getting 400 Bad Request?

A 400 Bad Request can occur when:

Request validation fails.
Required fields are missing.
Invalid data is supplied.
A banking business rule is violated.
An account withdrawal exceeds the available balance.

Check the response body for the exact error message.

18. Why am I getting 404 Not Found?

A 404 Not Found means that the requested resource does not exist.

For example:

GET /api/v1/employees/999

If employee 999 does not exist, the API returns:

404 Not Found

The same principle applies to account operations.

19. Which database does the application use?

The application uses:

Microsoft SQL Server

The database configuration is maintained in:

src/main/resources/application.yml

The application uses the Microsoft SQL Server JDBC driver to establish the database connection.

20. Does the application use HikariCP?

Yes.

HikariCP is used as the database connection pool.

The project contains configuration such as:

spring:
  datasource:
    hikari:
      pool-name: ShopDevHikariPool
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000

HikariCP improves database connection management by reusing existing connections instead of creating a new connection for every request.

21. What should I check if the database connection fails?

Check the following:

SQL Server service
SQL Server instance
Database name
JDBC URL
SQL Server JDBC driver
Integrated authentication configuration
Firewall/network connectivity

Also check the Spring Boot console for the actual SQLServerException.

For integrated authentication errors, verify that the required Microsoft JDBC authentication component is correctly configured for the JDBC driver version being used.

22. Does the project use Spring Boot Actuator?

Yes.

Spring Boot Actuator is used for application monitoring and operational information.

It can provide:

/actuator/health
/actuator/metrics
/actuator/info

Metrics can include:

JVM memory
JVM threads
HTTP requests
HikariCP connections
Disk usage
Application startup time

Actuator endpoints should be appropriately secured in non-development environments.

23. How do I check whether the application is healthy?

When Actuator health is exposed, use:

GET /actuator/health

For the local application:

http://localhost:8089/actuator/health

A healthy application normally returns:

{
  "status": "UP"
}

The health response can also contain components such as:

db
diskSpace
ping
redis
ssl

depending on the application's configured dependencies.

24. How do I start and test the application?
Start the application

Using Maven:

mvn spring-boot:run

Or run the main Spring Boot class from Eclipse.

After startup, verify:

GET http://localhost:8089/api/v1/employees

Expected:

200 OK
Test using Postman

Test the main flows:

Employee CRUD
       |
       v
Account Deposit
       |
       v
Account Balance
       |
       v
Account Withdrawal
       |
       v
Account Balance
25. Where should I look when something goes wrong?

Use the following order:

1. Check the API request in Postman
          |
          v
2. Check HTTP status and response body
          |
          v
3. Check Spring Boot console/logs
          |
          v
4. Check application.yml
          |
          v
5. Check SQL Server connectivity
          |
          v
6. Check database records
          |
          v
7. Check HikariCP/Actuator metrics

