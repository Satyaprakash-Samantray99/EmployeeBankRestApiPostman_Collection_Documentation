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
| employees                |
| accounts                 |
+--------------------------+
3. Layered Architecture
3.1 Client Layer

The Client Layer sends HTTP requests to the REST API.

The primary API testing client used in this project is:

Postman

Example:

GET http://localhost:8089/api/v1/employees

The client communicates with the Controller Layer through HTTP.

3.2 Controller Layer

The Controller Layer exposes REST endpoints for Employee and Account operations.

Main components:

EmployeeController
AccountController
Responsibilities
Receive HTTP requests.
Read path parameters.
Read request bodies.
Validate incoming requests.
Call the appropriate service.
Return HTTP responses.
Map application results to REST responses.
Employee Controller

The EmployeeController handles employee operations such as:

GET     /api/v1/employees
GET     /api/v1/employees/{id}
POST    /api/v1/employees
PUT     /api/v1/employees/{id}
DELETE  /api/v1/employees/{id}
Account Controller

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

Creating employees.
Retrieving employees.
Retrieving an employee by ID.
Updating employees.
Deleting employees.
Checking employee-related business rules.
Handling duplicate employee email scenarios.
AccountService

Responsible for:

Depositing money.
Withdrawing money.
Checking account balance.
Validating account existence.
Checking sufficient balance before withdrawal.
Applying banking business rules.

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

The primary database tables are:

employees
accounts

The database stores employee and account information used by the application.

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
              | EmployeeService          |
              | AccountService           |
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
              | employees               |
              | accounts                |
              +-------------------------+
5. Employee API Request Flow

The following diagram shows how an employee request is processed.

Postman
   |
   | POST /api/v1/employees
   |
   v
EmployeeController
   |
   | createEmployee()
   |
   v
EmployeeService
   |
   | Validate employee
   | Check duplicate email
   |
   v
EmployeeRepository
   |
   | save()
   |
   v
SQL Server
   |
   | INSERT employee
   |
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
   |
   v
Postman
6. Account Deposit Request Flow

The deposit operation follows the following flow:

Postman
   |
   | POST /api/v1/accounts/{id}/deposit
   |
   v
AccountController
   |
   | deposit()
   |
   v
AccountService
   |
   | Find Account
   |
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
   |
   | Update balance
   |
   v
AccountRepository
   |
   | save()
   |
   v
SQL Server
   |
   v
AccountController
   |
   | 200 OK
   |
   v
Postman
7. Account Withdrawal Request Flow

Withdrawal contains additional business validation.

Postman
   |
   | POST /api/v1/accounts/{id}/withdraw
   |
   v
AccountController
   |
   v
AccountService
   |
   | Find Account
   |
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
          Business Exception
                |
                v
          400 Bad Request

The application therefore prevents an account from being withdrawn beyond its available balance.

8. Database Architecture

The application uses SQL Server as the persistence layer.

+-----------------------+
|       employees       |
+-----------------------+
| PK  id                |
|     name              |
|     email             |
|     salary            |
+-----------------------+


+-----------------------+
|       accounts        |
+-----------------------+
| PK  id                |
|     account_number    |
|     balance           |
|     ...               |
+-----------------------+

The exact columns should match the Employee and Account entity classes in your project.

The application accesses these tables through JPA repositories rather than directly executing database operations from the Controller layer.

9. Exception Handling Architecture

The application handles API errors using the application's exception-handling mechanism.

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
     |                           |
     |                           v
     |                    EmployeeService
     |
     +---------- Invalid
                  |
                  v
           Error Handling
                  |
                  v
           400 Bad Request

Validation can include:

Name
Email
Salary

The exact validation rules are defined by the DTO/entity validation configuration in the project.

11. Employee and Account Module Architecture

The application contains two independent functional modules.

                    EmployeeBankRestApi
                           |
              +------------+------------+
              |                         |
              v                         v
       Employee Module            Account Module
              |                         |
              v                         v
     EmployeeController          AccountController
              |                         |
              v                         v
       EmployeeService            AccountService
              |                         |
              v                         v
     EmployeeRepository          AccountRepository
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
| Employee API Requests   |
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
| Account API Requests    |
|-------------------------|
| Deposit                 |
| Withdraw                |
| Get Balance             |
+------------+------------+
             |
             v
      Spring Boot API

Postman can be used to verify:

HTTP status codes.
Request validation.
Employee CRUD operations.
Account deposit operations.
Account withdrawal operations.
Insufficient balance handling.
Account balance retrieval.
Resource-not-found scenarios.
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
API Testing	Postman
Build Tool	Maven
14. Architecture Summary

The overall application request flow is:

                 Postman / Client
                        |
                        v
               REST Controller
                  /          \
                 /            \
                v              v
        EmployeeService    AccountService
                |              |
                v              v
        EmployeeRepository AccountRepository
                \              /
                 \            /
                  v          v
                JPA / Hibernate
                        |
                        v
                  SQL Server

The architecture separates responsibilities across the application layers:

Controller — handles HTTP requests and responses.
Service — contains business logic.
Repository — handles database access.
JPA/Hibernate — manages object-relational mapping.
SQL Server — stores persistent employee and account data.
Postman — provides API testing and validation.