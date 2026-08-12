# Employee Bank REST API - Change Log

All notable changes made to the Employee Bank REST API are documented in this file.

---

## [1.0.0] - Initial Release

### Added

#### Employee Management API

Added REST APIs for employee management:

```text
GET     /api/v1/employees
GET     /api/v1/employees/{id}
POST    /api/v1/employees
PUT     /api/v1/employees/{id}
DELETE  /api/v1/employees/{id}

Features include:

Create employee
Retrieve all employees
Retrieve employee by ID
Update employee
Delete employee
Pagination support
Employee validation
Duplicate email validation
Banking Account API

Added REST APIs for banking account operations:

POST    /api/v1/accounts/{id}/deposit
POST    /api/v1/accounts/{id}/withdraw
GET     /api/v1/accounts/{id}/balance

Features include:

Deposit money
Withdraw money
Check account balance
Account lookup
Insufficient balance validation
Business rule validation
Database Integration

Added Microsoft SQL Server database integration using:

Spring Data JPA
Hibernate
Microsoft SQL Server JDBC Driver

Database configuration is maintained in:

src/main/resources/application.yml
Validation

Added Jakarta Bean Validation for API request data.

Validation includes:

@NotBlank
@NotNull
@Email
@Positive

Invalid request data returns:

400 Bad Request
Exception Handling

Added centralized exception handling using:

@RestControllerAdvice

Handled scenarios include:

400 Bad Request
404 Not Found
409 Conflict
422 Unprocessable Entity
500 Internal Server Error

Handled exceptions include:

ResourceNotFoundException
DuplicateEmailException
BusinessException
MethodArgumentNotValidException
Exception
Pagination

Added pagination support for employee listing using Spring Data JPA Pageable.

Example:

GET /api/v1/employees?page=0&size=10
[1.1.0] - Logging and Observability
Added
SLF4J and Logback

Added application logging using:

SLF4J
Logback

Logging configuration is maintained using:

logback-spring.xml

Logging supports:

Console logging
File logging
Log levels
Rolling log files
Application error logging
MDC and Correlation ID

Added MDC-based correlation ID support for tracking requests across application logs.

Example:

correlationId
HTTP Logging

Added logging support for HTTP requests and responses.

Sensitive information such as passwords, tokens, and credentials must not be logged.

[1.2.0] - HikariCP Connection Pooling
Added

Configured HikariCP as the application's database connection pool.

Configuration includes:

pool-name
maximum-pool-size
minimum-idle
connection-timeout
max-lifetime

Added HikariCP monitoring through Spring Boot Actuator.

Important metrics include:

hikaricp.connections
hikaricp.connections.active
hikaricp.connections.idle
hikaricp.connections.pending
[1.3.0] - Spring Boot Actuator
Added

Added Spring Boot Actuator for application monitoring.

Configured endpoints include:

/actuator/health
/actuator/info
/actuator/metrics

Monitoring includes:

Application health
Database health
Disk space
JVM metrics
HTTP request metrics
HikariCP metrics

Added support for liveness and readiness monitoring.

[1.4.0] - Swagger / OpenAPI
Added

Added API documentation using Springdoc OpenAPI.

Configured documentation paths:

/api-docs
/swagger-ui.html

Swagger documentation covers:

Employee APIs
Account APIs
Request parameters
Request bodies
Response codes
Validation
[1.5.0] - Datadog Monitoring
Added

Added Datadog monitoring support using Micrometer.

Monitoring includes:

Application metrics
JVM metrics
HTTP metrics
Database metrics
HikariCP metrics

Added custom business metrics for:

Employee creation
Employee update
Employee deletion
Account deposit
Account withdrawal

Added timers for measuring operation execution time.

Monitoring supports:

API response time
Request count
Error rate
JVM health
Database metrics
Connection pool metrics
[1.6.0] - Environment Configuration
Added

Added environment-specific Spring Boot configuration:

application.yml
application-dev.yml
application-test.yml
application-prod.yml

Environment-specific configuration supports:

Database configuration
Server configuration
Logging configuration
Monitoring configuration
Application settings
Changed

Sensitive configuration values should be provided through environment variables or secure configuration rather than committed directly to source control.

[1.7.0] - Standardized Error Responses
Added

Added a standardized error response structure containing:

timestamp
status
message
errors
path

Handled business errors include:

Employee not found
Account not found
Duplicate employee email
Insufficient account balance
Invalid employee data
[1.8.0] - API Testing
Added

Added API testing using Postman.

Test scenarios include:

Employee CRUD
Account deposit
Account withdrawal
Account balance
Validation errors
Not found errors
Duplicate email
Insufficient balance

Added tests for expected HTTP status codes:

200 OK
201 Created
204 No Content
400 Bad Request
404 Not Found
409 Conflict
422 Unprocessable Entity
500 Internal Server Error
[1.9.0] - Database and Connection Improvements
Added

Improved SQL Server database and JDBC configuration.

Configured:

SQL Server JDBC Driver
Integrated Security
Encrypted connection
Trust Server Certificate
HikariCP connection pooling
Changed

Improved database connection handling for:

Connection reliability
Connection timeout
Connection pool management
Database resource usage
[2.0.0] - Build and Deployment
Added

Standardized Maven build process:

mvn clean package

The executable JAR is generated under:

target/

Application can be started using:

java -jar target/<application-name>.jar

Added deployment verification for:

Application startup
Database connectivity
API availability
Actuator health
API testing

Added rollback procedure for restoring a previously working application version.

[2.1.0] - Project Documentation
Added

Added project documentation covering:

API Design
Architecture
Database Schema
Environment Setup
Deployment
Runbook
Troubleshooting
FAQ
Jira Links

Documentation is maintained under:

docs/

Added database documentation covering:

Tables
Columns
Primary Keys
Foreign Keys
Constraints
JPA mappings
SQL Server configuration

Added troubleshooting documentation for:

SQL Server connection failures
Integrated authentication errors
Port conflicts
Validation errors
404 errors
500 errors
HikariCP issues
Actuator issues
Postman issues
Change Log Maintenance Rules

All significant changes to the Employee Bank REST API should be recorded in this file.

Changes that should be documented include:

New API endpoint
Changed API endpoint
Removed API endpoint
Database schema change
Entity change
Validation change
Exception handling change
Logging change
MDC change
HikariCP configuration change
Actuator configuration change
Datadog configuration change
Security change
Postman test change
Bug fix
Deployment change
Documentation change

Each future entry should contain:

Version
Date
Change type
Description
Related Jira ticket
Change Types
Added

For newly introduced functionality.

Changed

For changes to existing functionality.

Fixed

For bug fixes.

Removed

For removed functionality.

Deprecated

For functionality that will be removed in a future release.

Security

For security-related changes.

Future Change Template
## [Version] - YYYY-MM-DD

### Added

- Description of new functionality.
- Jira: EMP-XXX

### Changed

- Description of changed functionality.
- Jira: EMP-XXX

### Fixed

- Description of bug fix.
- Jira: EMP-XXX

### Removed

- Description of removed functionality.
- Jira: EMP-XXX

### Security

- Description of security-related change.
- Jira: EMP-XXX
Current Project Version Summary
1.0.0
└── Employee and Banking REST APIs
    ├── Employee CRUD
    ├── Account Deposit
    ├── Account Withdrawal
    ├── Account Balance
    ├── Validation
    ├── Exception Handling
    └── Pagination

1.1.0
└── Logging and Observability
    ├── SLF4J
    ├── Logback
    ├── MDC
    ├── Correlation ID
    └── HTTP Logging

1.2.0
└── HikariCP Connection Pooling

1.3.0
└── Spring Boot Actuator
    ├── Health
    ├── Metrics
    ├── JVM Monitoring
    └── Liveness / Readiness

1.4.0
└── Swagger / OpenAPI

1.5.0
└── Datadog Monitoring
    ├── Custom Metrics
    ├── Timers
    └── Application Monitoring

1.6.0
└── Environment Configuration

1.7.0
└── Standardized Error Responses

1.8.0
└── Postman API Testing

1.9.0
└── Database and Connection Improvements

2.0.0
└── Build and Deployment

2.1.0
└── Project Documentation