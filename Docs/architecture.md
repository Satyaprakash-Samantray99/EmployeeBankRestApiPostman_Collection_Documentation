EmployeeBankRestApi - System Architecture
1. Overview

The EmployeeBankRestApi is a Spring Boot REST application that provides APIs for managing employees and bank accounts.

The application follows a layered architecture:

Client Layer
Controller Layer
Service Layer
Repository Layer
Database Layer

The Employee module provides CRUD operations for employee records, while the Account module provides banking operations such as deposit, withdrawal, and balance inquiry.

This separation keeps HTTP request handling, business logic, data access, and database operations independent and maintainable.

2. High-Level Architecture
+--------------------------+
|     Postman / Client     |
+------------+-------------+
             |
             | HTTP Request
             v
+--------------------------+
|     Controller Layer     |
|--------------------------|
| EmployeeController       |
| AccountController        |
+------------+-------------+
             |
             v
+--------------------------+
|       Service Layer      |
|--------------------------|
| EmployeeService          |
| AccountService           |
+------------+-------------+
             |
             v
+--------------------------+
|     Repository Layer     |
|--------------------------|
| EmployeeRepository       |
| AccountRepository        |
+------------+-------------+
             |
             | JPA / Hibernate
             v
+--------------------------+
|      SQL Server          |
|--------------------------|
| employee_details7        |
| account_details7         |
+--------------------------+
3. Layered Architecture
3.1 Client Layer

The Client Layer sends HTTP requests to the REST API.

The primary API testing client used in this project is Postman.

Example:

GET http://localhost:8089/api/v1/employees

The client communicates with the Controller Layer through HTTP.

3.2 Controller Layer

The Controller Layer exposes REST endpoints for Employee and Account operations.

Main components:

EmployeeController
AccountController
Responsibilities
Receive HTTP requests
Read path parameters
Read request bodies
Validate incoming requests
Call the appropriate service
Return HTTP responses
Map application results to REST responses
EmployeeController

The EmployeeController handles employee operations:

GET     /api/v1/employees
GET     /api/v1/employees/{id}
POST    /api/v1/employees
PUT     /api/v1/employees/{id}
DELETE  /api/v1/employees/{id}
AccountController

The AccountController handles banking operations:

POST /api/v1/accounts/{id}/deposit
POST /api/v1/accounts/{id}/withdraw
GET  /api/v1/accounts/{id}/balance
3.3 Service Layer

The Service Layer contains the application's business logic.

Main components:

EmployeeService
AccountService
EmployeeService

Responsible for:

Creating employees
Retrieving employees
Retrieving an employee by ID
Updating employees
Deleting employees
Checking employee-related business rules
Handling duplicate employee email scenarios
AccountService

Responsible for:

Depositing money
Withdrawing money
Checking account balance
Validating account existence
Checking sufficient balance before withdrawal
Applying banking business rules

For example, withdrawal processing follows:

AccountController
       |
       v
AccountService
       |
       +---- Find Account
       |
       +---- Check Balance
       |
       +---- Validate Withdrawal Amount
       |
       +---- Update Balance
       |
       v
AccountRepository
3.4 Repository Layer

The Repository Layer communicates with SQL Server using Spring Data JPA.

Main components:

EmployeeRepository
AccountRepository

The repositories provide database access operations through Spring Data JPA.

Typical operations include:

save()
findAll()
findById()
delete()
existsById()

The repository layer keeps database access separate from the application's business logic.

3.5 Database Layer

The application uses Microsoft SQL Server as its relational database.

The primary tables used by the application are:

employee_details7
account_details7

The application communicates with SQL Server through:

Spring Data JPA
       |
       v
Hibernate
       |
       v
Microsoft SQL Server
4. Component Interaction Diagram
+----------------+
|    Postman     |
|     Client     |
+-------+--------+
        |
        | HTTP
        v
+-------------------------+
|    REST Controllers     |
|-------------------------|
| EmployeeController      |
| AccountController       |
+------------+------------+
             |
             v
+-------------------------+
|      Service Layer      |
|-------------------------|
| EmployeeService         |
| AccountService          |
+------------+------------+
             |
             v
+-------------------------+
|    Repository Layer     |
|-------------------------|
| EmployeeRepository      |
| AccountRepository       |
+------------+------------+
             |
             | JPA / Hibernate
             v
+-------------------------+
|     Microsoft SQL       |
|        Server           |
|-------------------------|
| employee_details7       |
| account_details7        |
+-------------------------+
5. Employee API Request Flow

The following diagram shows how an employee creation request is processed.

Postman
   |
   | POST /api/v1/employees
   v
EmployeeController
   |
   | addEmployee()
   v
EmployeeService
   |
   | Validate employee
   | Check duplicate email
   v
EmployeeRepository
   |
   | save()
   v
SQL Server
   |
   | INSERT employee
   v
EmployeeRepository
   |
   v
EmployeeService
   |
   v
EmployeeController
   |
   | 201 Created
   v
Postman
6. Account Deposit Request Flow

The deposit operation follows this flow:

Postman
   |
   | POST /api/v1/accounts/{id}/deposit
   v
AccountController
   |
   | deposit()
   v
AccountService
   |
   | Find Account
   v
AccountRepository
   |
   v
SQL Server
   |
   | Account details
   v
AccountService
   |
   | Add deposit amount
   | Update balance
   v
AccountRepository
   |
   | save()
   v
SQL Server
   |
   v
AccountController
   |
   | 200 OK
   v
Postman
7. Account Withdrawal Request Flow

Withdrawal contains additional business validation.

Postman
   |
   | POST /api/v1/accounts/{id}/withdraw
   v
AccountController
   |
   v
AccountService
   |
   | Find Account
   v
AccountRepository
   |
   v
SQL Server
   |
   | Current Balance
   v
AccountService
   |
   | Check withdrawal amount
   |
   +---- Sufficient Balance ----+
   |                            |
   |                            v
   |                     Update Balance
   |                            |
   |                            v
   |                     AccountRepository
   |                            |
   |                            v
   |                       SQL Server
   |
   +---- Insufficient Balance
                |
                v
        BusinessException
                |
                v
         400 Bad Request

The application prevents an account from being withdrawn beyond its available balance.

8. Database Architecture

The application uses SQL Server as the persistence layer.

Employee Table
+-----------------------------+
|      employee_details7      |
+-----------------------------+
| PK  id                      |
|     name                    |
|     email                   |
|     department               |
|     salary                  |
+-----------------------------+
Account Table
+-----------------------------+
|       account_details7      |
+-----------------------------+
| PK  id                      |
|     account_holder_name     |
|     account_number          |
|     balance                 |
+-----------------------------+

The application accesses these tables through JPA repositories rather than directly executing database operations from the Controller layer.

9. Exception Handling Architecture

The application handles API errors using its exception-handling mechanism.

The general flow is:

Client Request
      |
      v
Controller
      |
      v
Service
      |
      | Exception
      v
Exception Handling
      |
      v
Error Response
      |
      v
Client

Common error scenarios include:

400 Bad Request
|
+-- Invalid input
+-- Validation failure
+-- Insufficient account balance

404 Not Found
|
+-- Employee not found
+-- Account not found

409 Conflict
|
+-- Duplicate employee email

422 Unprocessable Entity
|
+-- Business rule violation

500 Internal Server Error
|
+-- Unexpected application error
10. Employee Validation Flow

Employee creation and update requests are validated before business processing.

JSON Request
      |
      v
EmployeeController
      |
      | @Valid
      v
Request Validation
      |
      +---------- Valid ----------+
      |                            |
      |                            v
      |                    EmployeeService
      |
      +---------- Invalid
                   |
                   v
             Error Handling
                   |
                   v
             400 Bad Request

Validation includes fields such as:

Name
Email
Department
Salary

The exact validation rules are defined in the DTO validation configuration.

11. Employee and Account Module Architecture

The application contains two functional modules.

                    EmployeeBankRestApi
                            |
               +------------+------------+
               |                         |
               v                         v
        Employee Module            Account Module
               |                         |
               v                         v
      EmployeeController         AccountController
               |                         |
               v                         v
        EmployeeService             AccountService
               |                         |
               v                         v
      EmployeeRepository         AccountRepository
               |                         |
               +------------+------------+
                            |
                            v
                       SQL Server

This separation allows employee management and banking operations to have their own business logic while using the same application and database infrastructure.

12. Postman API Testing Architecture

Postman is used to test the REST endpoints.

+-------------------------+
|     Postman Client      |
+------------+------------+
             |
             v
+-------------------------+
|     Employee APIs       |
|-------------------------|
| Get All Employees       |
| Get Employee By ID      |
| Create Employee         |
| Update Employee         |
| Delete Employee         |
+------------+------------+
             |
             |
             v
+-------------------------+
|      Account APIs       |
|-------------------------|
| Deposit                 |
| Withdraw                |
| Get Balance             |
+------------+------------+
             |
             v
+-------------------------+
|     Spring Boot API     |
+-------------------------+

Postman can be used to verify:

HTTP status codes
Request validation
Employee CRUD operations
Account deposit operations
Account withdrawal operations
Insufficient balance handling
Account balance retrieval
Resource-not-found scenarios
13. Technology Stack
Component	Technology
Backend	Spring Boot
Programming Language	Java 21
REST API	Spring Web
Persistence	Spring Data JPA
ORM	Hibernate
Database	Microsoft SQL Server
Database Driver	Microsoft SQL Server JDBC Driver
Validation	Jakarta Validation
Caching	Redis
Connection Pool	HikariCP
API Testing	Postman
Build Tool	Maven
API Documentation	Swagger / OpenAPI
14. Architecture Summary

The overall application request flow is:

                 Postman / Client
                        |
                        v
                REST Controller
                   /        \
                  /          \
                 v            v
        EmployeeService    AccountService
                |                |
                v                v
        EmployeeRepository  AccountRepository
                \                /
                 \              /
                  v            v
                  JPA / Hibernate
                        |
                        v
                   SQL Server

The architecture separates responsibilities across the application layers:

Layer	Responsibility
Controller	Handles HTTP requests and responses
Service	Contains business logic
Repository	Handles database access
JPA/Hibernate	Manages object-relational mapping
SQL Server	Stores persistent employee and account data
Redis	Provides caching for frequently accessed data
Postman	Provides API testing and validation