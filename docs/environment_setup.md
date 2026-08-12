# EmployeeBankRestApi - Environment Setup Guide

## Table of Contents

1.  Purpose](#1-purpose)
2.  Technology Requirements](#2-technology-requirements)
3.  Verify Java](#3-verify-java)
4.  Verify Maven](#4-verify-maven)
5.  Import Project into Eclipse](#5-import-project-into-eclipse)
6.  Update Maven Dependencies](#6-update-maven-dependencies)
7.  Required Maven Dependencies](#7-required-maven-dependencies)
8.  SQL Server Setup](#8-sql-server-setup)
9.  Create Application Database](#9-create-application-database)
10. Configure Spring Boot Datasource](#10-configure-spring-boot-datasource)
11. SQL Server Integrated Authentication](#11-sql-server-integrated-authentication)
12. JPA Configuration](#12-jpa-configuration)
13. Employee Database Table](#13-employee-database-table)
14. Account Database Table](#14-account-database-table)
15. Verify Database Tables](#15-verify-database-tables)
16. Verify Employee Data](#16-verify-employee-data)
17. Verify Account Data](#17-verify-account-data)
18. Configure Application Port](#18-configure-application-port)
19. Verify Port 8089](#19-verify-port-8089)
20. Start the Application from Eclipse](#20-start-the-application-from-eclipse)
21. Start the Application Using Maven](#21-start-the-application-using-maven)
22. Start the Packaged Application](#22-start-the-packaged-application)
23. Verify Application Startup](#23-verify-application-startup)
24. Verify Employee API](#24-verify-employee-api)
25. Verify Employee CRUD APIs](#25-verify-employee-crud-apis)
26. Verify Account API](#26-verify-account-api)
27. Verify Deposit API](#27-verify-deposit-api)
28. Verify Withdrawal API](#28-verify-withdrawal-api)
29. Verify Insufficient Balance](#29-verify-insufficient-balance)
30. Verify Account Balance API](#30-verify-account-balance-api)
31. Postman Setup](#31-postman-setup)
32. Create Postman Environment](#32-create-postman-environment)
33. Environment Variable Usage](#33-environment-variable-usage)
34. Postman Request Chaining](#34-postman-request-chaining)
35. Run Postman Tests](#35-run-postman-tests)
36. First-Run Verification](#36-first-run-verification)
37. Common Environment Setup Problems](#37-common-environment-setup-problems)
38. Environment Setup Flow](#38-environment-setup-flow)
39. Final Environment Checklist](#39-final-environment-checklist)
40. Environment Setup Complete](#40-environment-setup-complete)

---

## 1. Purpose

This document describes how to configure the development environment for the **EmployeeBankRestApi** Spring Boot application.

The setup includes:

- Java 21
- Maven
- Eclipse / Spring Tool Suite
- Microsoft SQL Server
- SQL Server Management Studio
- Database configuration
- Spring Boot configuration
- Postman
- Employee API testing
- Account API testing
- First-run verification

The application provides REST APIs for:

- Employee management
- Bank account management
- Deposit operations
- Withdrawal operations
- Account balance inquiry

---

## 2. Technology Requirements

| Tool | Purpose |
|---|---|
| Java 21 | Run the Spring Boot application |
| Maven | Dependency management and project build |
| Eclipse / STS | Development IDE |
| Spring Boot | Application framework |
| Microsoft SQL Server | Application database |
| SQL Server Management Studio | Database administration |
| Postman | REST API testing |

---

## 3. Verify Java

Open Command Prompt:

```bash
java -version

The project uses Java 21.

Also verify the Java compiler:

javac -version

Expected output:

java version "21..."
javac 21...

If Java is not recognized, install the required JDK and configure:

JAVA_HOME
PATH
4. Verify Maven

Run:

mvn -version

The output should display:

Maven version
Java version
Java home
Operating system information

Verify that Maven is using Java 21.

Example:

Apache Maven ...
Java version: 21
Java home: ...
5. Import Project into Eclipse

Open Eclipse or Spring Tool Suite.

Select:

File
  ↓
Import
  ↓
Maven
  ↓
Existing Maven Projects

Select the EmployeeBankRestApi project directory.

Eclipse will detect the pom.xml file and import the Maven project.

Allow Maven to download all required dependencies.

6. Update Maven Dependencies

If dependencies are not resolved correctly:

Right-click Project
        ↓
Maven
        ↓
Update Project
        ↓
Select EmployeeBankRestApi
        ↓
OK

After the update, verify that there are no dependency errors in the project.

7. Required Maven Dependencies

The project uses dependencies for the following major components:

Spring Web
Spring Data JPA
Spring Boot Validation
Microsoft SQL Server JDBC Driver
Lombok
Spring Boot DevTools
Spring Boot Actuator

The dependencies are managed through:

pom.xml

Maven automatically downloads the required libraries.

8. SQL Server Setup

Microsoft SQL Server must be installed and running before starting the application.

Open SQL Server Management Studio and connect to the SQL Server instance configured for the project.

Verify that the SQL Server service is running.

The application uses SQL Server for:

Employee data
Account data
9. Create Application Database

Create the database used by the EmployeeBankRestApi application.

Example:

CREATE DATABASE EmployeeBankDB;
GO

Verify that the database exists:

SELECT name
FROM sys.databases
WHERE name = 'EmployeeBankDB';

Expected result:

EmployeeBankDB

If the project is already configured with a different database name in application.yml, use that configured database name instead.

10. Configure Spring Boot Datasource

The main configuration file is:

src/main/resources/application.yml

The application uses Microsoft SQL Server.

Example datasource configuration:

spring:
  datasource:
    url: jdbc:sqlserver://<SERVER>\<INSTANCE>;databaseName=<DATABASE>;integratedSecurity=true;encrypt=true;trustServerCertificate=true
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver

Replace:

<SERVER>
<INSTANCE>
<DATABASE>

with the values configured for the development environment.

11. SQL Server Integrated Authentication

The application can use Windows Integrated Authentication through:

integratedSecurity=true

When integrated authentication is enabled, the application uses the Windows account under which the application is running to authenticate with SQL Server.

If the following error occurs:

This driver is not configured for integrated authentication

verify:

SQL Server JDBC Driver
SQL Server JDBC authentication component
JVM architecture
Authentication library architecture
JAVA_HOME
java.library.path

For the SQL Server JDBC driver version being used by the project, ensure the corresponding authentication DLL is correctly available to the JVM.

12. JPA Configuration

The application uses Spring Data JPA and Hibernate.

The JPA configuration is maintained in:

src/main/resources/application.yml

Example:

spring:
  jpa:
    hibernate:
      ddl-auto: update

The application uses Hibernate to map Java entities to SQL Server tables.

The primary application tables are:

employees
accounts
13. Employee Database Table

The Employee module stores employee information in:

employees

The table contains employee-related information such as:

id
name
email
salary

The exact database structure is created or updated according to the Employee JPA entity.

14. Account Database Table

The Account module stores bank account information in:

accounts

The table contains account-related information such as:

id
account_number
balance

The exact database structure is created or updated according to the Account JPA entity.

15. Verify Database Tables

After starting the application, open SQL Server Management Studio and execute:

SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE';

Verify that the application tables are available.

Expected application tables include:

employees
accounts
16. Verify Employee Data

Execute:

SELECT *
FROM employees;

To find a specific employee:

SELECT *
FROM employees
WHERE id = 1;
17. Verify Account Data

Execute:

SELECT *
FROM accounts;

To find a specific account:

SELECT *
FROM accounts
WHERE id = 1;

To check an account balance:

SELECT
    id,
    balance
FROM accounts
WHERE id = 1;
18. Configure Application Port

The application is configured to run on:

8089

The configuration is maintained using:

server:
  port: 8089

Therefore, the local application base URL is:

http://localhost:8089

19. Verify Port 8089

Before starting the application, verify that port 8089 is available.

On Windows:

netstat -ano | findstr :8089

If another application is already using the port, stop the appropriate process or configure another available port.

20. Start the Application from Eclipse

Locate the Spring Boot main application class in the project.

For example:

EmployeeApiApplication.java

Then:

Right-click
    ↓
Run As
    ↓
Spring Boot App

Eclipse will start the Spring Boot application.

21. Start the Application Using Maven

The application can also be started from the project root using:

mvn spring-boot:run

The application will start using the configuration from:

src/main/resources/application.yml
22. Start the Packaged Application

First build the project:

mvn clean package

Then locate the generated JAR inside:

target/

Run the generated JAR:

java -jar target/<generated-jar-name>.jar

The exact JAR name depends on the project version configured in pom.xml.

23. Verify Application Startup

Check the Eclipse console or terminal.

A successful Spring Boot startup should contain a message similar to:

Started EmployeeApiApplication

The exact application class name depends on the project's main Spring Boot class.

The application should also successfully initialize:

Spring Boot
     |
     v
Tomcat
     |
     v
Spring Data JPA
     |
     v
Hibernate
     |
     v
SQL Server
24. Verify Employee API

Once the application has started, open Postman.

The Employee API base URL is:

http://localhost:8089/api/v1

Verify:

GET http://localhost:8089/api/v1/employees

Expected:

200 OK

A database containing no employee records may return:

[]
25. Verify Employee CRUD APIs

The Employee module provides:

Operation	Method	Endpoint	Expected Status
Get all employees	GET	/api/v1/employees	200 OK
Get employee by ID	GET	/api/v1/employees/{id}	200 OK
Create employee	POST	/api/v1/employees	201 Created
Update employee	PUT	/api/v1/employees/{id}	200 OK
Delete employee	DELETE	/api/v1/employees/{id}	204 No Content
26. Verify Account API

The Account module provides:

POST /api/v1/accounts/{id}/deposit
POST /api/v1/accounts/{id}/withdraw
GET  /api/v1/accounts/{id}/balance

These APIs can be tested using Postman.

27. Verify Deposit API

Example:

POST http://localhost:8089/api/v1/accounts/1/deposit

Provide the required deposit request body according to the Account API implementation.

Expected successful response:

200 OK

Verify the updated balance in SQL Server:

SELECT
    id,
    balance
FROM accounts
WHERE id = 1;

The balance should increase by the deposited amount.

28. Verify Withdrawal API

Example:

POST http://localhost:8089/api/v1/accounts/1/withdraw

Provide the required withdrawal amount.

Expected successful response:

200 OK

After a successful withdrawal, verify:

SELECT
    id,
    balance
FROM accounts
WHERE id = 1;

The balance should decrease by the withdrawal amount.

29. Verify Insufficient Balance

Test the business rule by attempting to withdraw more than the available balance.

Example:

Current Balance = 5,000
Withdrawal      = 7,000

The operation should be rejected.

Expected response:

400 Bad Request

Example message:

Insufficient balance.

This verifies that the Account Service correctly enforces the insufficient-balance business rule.

30. Verify Account Balance API

Use:

GET http://localhost:8089/api/v1/accounts/1/balance

Expected:

200 OK

The response should contain the current balance of the requested account.

If the account does not exist:

404 Not Found
31. Postman Setup

Open Postman.

Create or import the EmployeeBankRestApi collection.

The collection should contain requests for the two application modules:

Employee APIs
    |
    +-- Get All Employees
    +-- Get Employee By ID
    +-- Create Employee
    +-- Update Employee
    +-- Delete Employee

Account APIs
    |
    +-- Deposit
    +-- Withdraw
    +-- Get Balance
32. Create Postman Environment

Create an environment named:

EmployeeBankRestApi Local

Configure the base URL:

Variable	Value
baseUrl	http://localhost:8089

If the Postman collection uses additional variables, configure them according to the collection.

For example:

Variable	Initial Value
employeeId	Empty
accountId	Empty

Select:

EmployeeBankRestApi Local

before executing the requests.

33. Environment Variable Usage

API requests should use:

{{baseUrl}}

For example:

{{baseUrl}}/api/v1/employees

Employee-specific requests can use:

{{employeeId}}

For example:

{{baseUrl}}/api/v1/employees/{{employeeId}}

Account-specific requests can use:

{{accountId}}

For example:

{{baseUrl}}/api/v1/accounts/{{accountId}}/balance

This allows the same Postman collection to be used without manually changing the server URL in every request.

34. Postman Request Chaining

The Postman collection can store IDs returned from successful requests.

After creating an employee, the returned employee ID can be stored as:

employeeId

The following requests can then use:

{{employeeId}}

Similarly, after creating or identifying an account, the account ID can be stored as:

accountId

The Account requests can then use:

{{accountId}}
Employee Flow
Create Employee
       |
       v
Store employeeId
       |
       v
Get Employee
       |
       v
Update Employee
       |
       v
Delete Employee
Account Flow
Account
   |
   v
Deposit
   |
   v
Check Balance
   |
   v
Withdraw
   |
   v
Check Balance
35. Run Postman Tests

Before running the collection, verify:

SQL Server              → Running
Spring Boot Application → Running
Port 8089               → Available
Postman Environment     → Selected
baseUrl                 → Correct

Run the EmployeeBankRestApi collection.

Verify that:

Employee APIs
Account APIs

are returning the expected HTTP status codes.

36. First-Run Verification

Perform the following checks after the initial environment setup:

1. Start SQL Server
        |
        v
2. Verify application database
        |
        v
3. Start EmployeeBankRestApi
        |
        v
4. Check Spring Boot startup logs
        |
        v
5. Verify SQL Server connection
        |
        v
6. GET /api/v1/employees
        |
        v
7. Verify Employee CRUD APIs
        |
        v
8. Verify Account Deposit
        |
        v
9. Verify Account Withdrawal
        |
        v
10. Verify Account Balance
        |
        v
11. Test insufficient balance
        |
        v
12. Verify Postman collection
37. Common Environment Setup Problems
37.1 Java Not Recognized

If:

java -version

returns:

'java' is not recognized...

verify that:

JAVA_HOME
PATH

are correctly configured.

Restart Command Prompt after changing environment variables.

37.2 Maven Not Recognized

If:

mvn -version

returns:

'mvn' is not recognized...

verify Maven installation and the system PATH.

37.3 SQL Server Connection Failure

Check:

SQL Server service
SQL Server instance
Database name
JDBC URL
SQL Server JDBC driver
Integrated authentication
Firewall / network connectivity
37.4 Integrated Authentication Error

If the application reports:

This driver is not configured for integrated authentication

verify the SQL Server JDBC authentication component and ensure that the appropriate authentication DLL is accessible to the JVM.

Also verify that the Java architecture and authentication library architecture are compatible.

37.5 Port 8089 Already in Use

Run:

netstat -ano | findstr :8089

Identify the process using the port.

Stop the process if appropriate or configure another port in:

server:
  port: 8089
37.6 Application Starts but API Fails

Check:

Spring Boot startup logs
Database connection
Controller mappings
Application port
Postman baseUrl
Request URL
Request body

Confirm that Postman is using:

http://localhost:8089

and not another port.

38. Environment Setup Flow
Install Java 21
       |
       v
Verify Maven
       |
       v
Configure Eclipse / STS
       |
       v
Import EmployeeBankRestApi
       |
       v
Resolve Maven Dependencies
       |
       v
Install / Configure SQL Server
       |
       v
Create Application Database
       |
       v
Configure application.yml
       |
       v
Verify SQL Server Connectivity
       |
       v
Start Spring Boot Application
       |
       v
Verify Port 8089
       |
       v
Verify Employee APIs
       |
       v
Verify Account APIs
       |
       v
Configure Postman
       |
       v
Run API Tests
       |
       v
Environment Ready
39. Final Environment Checklist
Development Environment
 Java 21 installed
 java -version verified
 javac -version verified
 Maven installed
 mvn -version verified
 Eclipse / STS configured
 EmployeeBankRestApi imported
 Maven dependencies resolved
 No compilation errors
Database Environment
 SQL Server installed
 SQL Server running
 Application database created
 application.yml configured
 SQL Server JDBC driver available
 Integrated authentication configured
 Database connection verified
 employees table available
 accounts table available
Spring Boot Application
 Application starts successfully
 No critical startup errors
 Port 8089 available
 SQL Server connection successful
 GET /api/v1/employees returns 200
 Employee CRUD APIs verified
 Deposit API verified
 Withdrawal API verified
 Balance API verified
 Insufficient balance scenario verified
Postman
 Postman installed
 EmployeeBankRestApi collection available
 EmployeeBankRestApi Local environment configured
 baseUrl configured
 employeeId configured where required
 accountId configured where required
 Employee requests verified
 Account requests verified
 Expected status codes verified
40. Environment Setup Complete

The development environment for EmployeeBankRestApi is considered ready when:

Java 21 is available
        +
Maven build succeeds
        +
Eclipse / STS project is configured
        +
SQL Server is running
        +
Application database is accessible
        +
Spring Boot starts successfully
        +
Application runs on port 8089
        +
Employee APIs work correctly
        +
Account APIs work correctly
        +
Deposit / Withdrawal business rules work
        +
Postman API tests pass
        =
Environment Ready