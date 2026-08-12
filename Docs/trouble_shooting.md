# Employee & Account API - Troubleshooting Guide

## Table of Contents

1. Purpose
2. Troubleshooting Flow
3. Spring Boot Application Does Not Start
4. Port 8089 Already in Use
5. SQL Server Connection Failure
6. Integrated Authentication Error
7. Database Does Not Exist
8. Tables Are Missing
9. HTTP 400 Bad Request
10. Employee Validation Failure
11. HTTP 404 Not Found
12. HTTP 409 Conflict
13. HTTP 422 Unprocessable Entity
14. HTTP 500 Internal Server Error
15. Duplicate Employee Email
16. Account Withdrawal Fails
17. Account Deposit Does Not Update Balance
18. Postman baseUrl Not Working
19. Postman Request Returns 404
20. Postman Request Body Validation Fails
21. Postman Tests Fail
22. Newman Command Not Recognized
23. Node Command Not Recognized
24. Newman Invalid URI Error
25. Maven Build Failure
26. HikariCP Connection Pool Problem
27. Quick Troubleshooting Matrix
28. Information to Collect Before Escalation
29. Troubleshooting Complete
---

## 2. Troubleshooting Flow

When an issue occurs, follow this sequence:

```text
Problem Occurs
      |
      v
Check Error Message
      |
      v
Check Spring Boot Console
      |
      v
Check application.yml
      |
      v
Check SQL Server
      |
      v
Check API Request
      |
      v
Check Postman/Newman
      |
      v
Apply Fix
      |
      v
Restart / Retest
3. Spring Boot Application Does Not Start
Symptoms

The application terminates during startup or does not display a successful startup message.

Expected message:

Started EmployeeApiApplication
Possible Causes
SQL Server is not running
Database does not exist
Incorrect datasource configuration
Port 8089 is already in use
Java configuration problem
JDBC authentication problem
Missing Maven dependency
Invalid application.yml
HikariCP connection problem
Resolution

Check the Spring Boot console and locate the first meaningful exception.

Verify Java:

java -version

The project uses Java 21.

Verify Maven:

mvn -version

Then check:

src/main/resources/application.yml

Verify that SQL Server is running and the configured database is available.

4. Port 8089 Already in Use
Error

The application may report that port 8089 is already in use.

Check the Port

On Windows:

netstat -ano | findstr :8089

Example:

TCP    0.0.0.0:8089    ...    LISTENING    12345

Here:

12345 = PID
Resolution

If the process can safely be terminated:

taskkill /PID 12345 /F

Then restart the Spring Boot application.

Warning: Do not terminate an unknown process without first confirming what it is.

5. SQL Server Connection Failure
Symptoms

Spring Boot fails during startup with a datasource, JDBC, or SQL Server error.

Possible errors include:

SQLServerException
Connection refused
Connection timed out
Receive timed out
Cannot open database
Login failed
Possible Causes
SQL Server is stopped
Incorrect server/instance
Incorrect database name
Incorrect JDBC URL
Firewall/network problem
Integrated authentication problem
JDBC driver problem
Resolution

Verify SQL Server is running.

Verify the available databases:

SELECT name
FROM sys.databases;

Check that the configured database exists.

Verify the datasource configuration in:

src/main/resources/application.yml

The JDBC URL must point to the correct SQL Server instance and database.

6. Integrated Authentication Error
Possible Error
This driver is not configured for integrated authentication
Cause

The project uses Windows Integrated Authentication through:

integratedSecurity=true

The Microsoft SQL Server JDBC driver therefore requires the appropriate authentication component.

Resolution

Verify:

Microsoft SQL Server JDBC driver
SQL Server JDBC authentication DLL
Java architecture
DLL architecture
Windows account permissions

The JDBC driver and native authentication component must be compatible.

For example, if using a 64-bit Java installation, use the appropriate 64-bit authentication DLL.

Also verify that the native library is available through the configured Java library path or system PATH.

Restart Eclipse after correcting the configuration.

7. Database Does Not Exist
Symptoms

The application cannot connect to the configured database.

Resolution

Open SQL Server Management Studio and verify the database.

For example:

SELECT name
FROM sys.databases
WHERE name = 'shopDb';

If the database is required but does not exist, create it:

CREATE DATABASE shopDb;
GO

Use the exact database name configured in application.yml.

Restart the application after creating the database.

8. Tables Are Missing
Symptoms

The database exists, but application tables are missing.

Check Configuration

Verify:

spring:
  jpa:
    hibernate:
      ddl-auto: update

The project uses Hibernate/JPA to create or update the schema based on the entity mappings.

Verify Tables

Run:

SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE';

Check that the Employee and Account-related tables are available.

9. HTTP 400 Bad Request
Meaning

A 400 Bad Request generally means that the request contains invalid input or fails request validation.

Possible Causes
Required field missing
Blank field
Invalid email
Invalid salary
Invalid account amount
Malformed JSON
Invalid request parameter

For Employee requests, check fields such as:

name
email
designation
salary

For Account operations, check:

amount
account ID
request format
10. Employee Validation Failure
Example Invalid Employee Request
{
  "name": "",
  "email": "invalid-email",
  "designation": "",
  "salary": -100
}

The request can fail because:

Name is blank
Email format is invalid
Designation is blank
Salary is not positive
Valid Example
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "designation": "Software Engineer",
  "salary": 50000
}
11. HTTP 404 Not Found
Meaning

The requested Employee or Account does not exist.

Employee Example
GET /api/v1/employees/999

If employee 999 does not exist, the application returns:

404 Not Found
Account Example
GET /api/v1/accounts/999/balance

may return:

404 Not Found
Resolution

Verify the ID in SQL Server.

For Employee:

SELECT *
FROM employees
WHERE id = 999;

For Account:

SELECT *
FROM accounts
WHERE id = 999;

Use an existing ID or create the required resource first.

12. HTTP 409 Conflict
Meaning

A 409 Conflict indicates that the request conflicts with an existing resource.

A common example is a duplicate Employee email.

Example
rahul@example.com

If another employee already uses this email, the application should reject the duplicate according to the project's business rules.

Resolution

Check existing employees:

SELECT *
FROM employees
WHERE email = 'rahul@example.com';

Use a different email address.

13. HTTP 422 Unprocessable Entity
Meaning

A 422 Unprocessable Entity response indicates that the request is syntactically valid but violates a business rule.

For example, an Account withdrawal may fail because:

Requested withdrawal amount > available balance

The application can return a business error such as:

Insufficient balance.
Resolution

Check the current account balance:

GET /api/v1/accounts/{id}/balance

Then request a withdrawal amount that satisfies the account's business rules.

Note: If the current implementation maps insufficient balance to 400 Bad Request, follow the application's actual configured behavior.

14. HTTP 500 Internal Server Error
Meaning

A 500 Internal Server Error indicates an unexpected server-side exception.

Possible Causes
Database failure
Unexpected NullPointerException
SQL exception
Database constraint violation
HikariCP connection problem
Unexpected application exception
Resolution

Check the Spring Boot console.

Look for the root exception and stack trace.

Do not troubleshoot only from the HTTP 500 response. The application log normally contains the actual cause.

15. Duplicate Employee Email

The Employee email is required to be unique according to the application's business/data rules.

Check existing values:

SELECT id, name, email
FROM employees
WHERE email = 'rahul@example.com';

If the email already exists, use another email.

For automated Postman testing, a dynamic email can be generated if required:

employee{{$timestamp}}@example.com
16. Account Withdrawal Fails
Symptom

The withdrawal request returns an error such as:

Insufficient balance.
Cause

The requested amount is greater than the available account balance.

Example:

Available Balance = 5,000
Withdrawal Amount = 8,000

The withdrawal should not be completed.

Resolution

First check:

GET /api/v1/accounts/{id}/balance

Then make a withdrawal within the available balance.

17. Account Deposit Does Not Update Balance

If a deposit appears successful but the balance does not change, verify:

Account ID
Deposit amount
Service logic
Repository save operation
Database transaction
Database record

Verify the account directly:

SELECT *
FROM accounts
WHERE id = 1;

Then check the application console for SQL statements or exceptions.

18. Postman baseUrl Not Working
Symptoms

A request using:

{{baseUrl}}/api/v1/employees

does not resolve correctly.

Resolution

Verify that the correct Postman environment is selected.

For local execution, the base URL should point to:

http://localhost:8089

Therefore:

{{baseUrl}}/api/v1/employees

should resolve to:

http://localhost:8089/api/v1/employees

Also verify that Spring Boot is running on port 8089.

19. Postman Request Returns 404

First verify that the URL matches the application's actual endpoint.

Employee Endpoints
GET    /api/v1/employees
GET    /api/v1/employees/{id}
POST   /api/v1/employees
PUT    /api/v1/employees/{id}
DELETE /api/v1/employees/{id}
Account Endpoints
POST /api/v1/accounts/{id}/deposit
POST /api/v1/accounts/{id}/withdraw
GET  /api/v1/accounts/{id}/balance

Check for mistakes such as:

/api/employees

instead of:

/api/v1/employees
20. Postman Request Body Validation Fails

If Postman receives:

400 Bad Request

check the request body.

Example:

{
  "name": "John",
  "email": "john@example.com",
  "designation": "Developer",
  "salary": 50000
}

Verify:

Content-Type: application/json

Also verify that all required fields are present and contain valid values.

21. Postman Tests Fail

If a Postman test fails, check:

[ ] Spring Boot application is running
[ ] Correct environment is selected
[ ] baseUrl is correct
[ ] Endpoint URL is correct
[ ] HTTP method is correct
[ ] Request body is valid
[ ] Required ID exists
[ ] Database is available

Do not immediately change the Postman assertion.

First compare the expected API behavior with the actual response.

22. Newman Command Not Recognized
Error
'newman' is not recognized
Resolution

Verify Node.js:

node -v

Verify npm:

npm -v

Install Newman:

npm install -g newman

Then verify:

newman --version

If Newman was installed but is still not recognized, open a new Command Prompt so the updated PATH can be loaded.

23. Node Command Not Recognized
Error
'node' is not recognized as an internal or external command
Resolution

Install Node.js.

After installation, close the current Command Prompt and open a new one.

Verify:

node -v

Then:

npm -v

If the commands still do not work, verify that the Node.js installation directory is included in the Windows PATH.

24. Newman Invalid URI Error
Error

Example:

Invalid URI "http:///api/v1/employees"
Cause

The baseUrl variable is missing or empty.

Resolution

Provide the base URL explicitly when running Newman:

newman run "Employee API.postman_collection.json" -e "Employee API Local.postman_environment.json" --env-var "baseUrl=http://localhost:8089"

Verify that the resulting request URL is:

http://localhost:8089/api/v1/employees
25. Maven Build Failure

Run:

mvn clean package

If the build fails:

Read the first meaningful error.
Check the Java version.
Check pom.xml.
Check compilation errors.
Check missing dependencies.
Correct the problem.
Run the build again.

Verify Java:

java -version

Verify Maven:

mvn -version

A successful build should generate the application JAR under:

target/
26. HikariCP Connection Pool Problem

The application uses HikariCP as the datasource connection pool.

Possible Symptoms
Connection is not available
Connection timeout
Pool exhausted
SQL connection timeout

Check the HikariCP configuration in:

application.yml

Important properties include:

spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      max-lifetime: 1800000

If the pool becomes exhausted, check:

Database availability
Long-running queries
Connection leaks
Maximum pool size
Application load

The application should be restarted only after identifying the underlying cause where possible.

27. Quick Troubleshooting Matrix
Problem	Likely Cause	Action
Application won't start	Configuration/database	Check Spring Boot console
Port 8089 unavailable	Another process running	Check netstat
SQL connection fails	SQL Server/configuration	Check datasource
Integrated authentication fails	JDBC authentication configuration	Check authentication DLL and architecture
400 response	Validation/input error	Check request body
404 response	Invalid Employee/Account ID or URL	Check endpoint and database
409 response	Duplicate resource	Check unique values
422 response	Business rule violation	Check business condition
500 response	Server/database error	Check application logs
Withdrawal fails	Insufficient balance	Check account balance
Deposit issue	Account/service/database problem	Check transaction and database
Postman URL fails	Incorrect baseUrl	Check environment
Postman tests fail	Incorrect response/request	Check response and console
Newman not recognized	Newman/PATH problem	Install Newman
Node not recognized	Node.js/PATH problem	Install/configure Node.js
Newman Invalid URI	Missing baseUrl	Provide baseUrl
Maven build fails	Compilation/dependency/configuration	Check Maven error
HikariCP timeout	Pool/database issue	Check database and pool configuration
28. Information to Collect Before Escalation

If the issue cannot be resolved, collect the following information:

Application version
Java version
Environment
Date/time of failure
Endpoint
HTTP method
Request body
HTTP status
Response body
Spring Boot error
SQL Server error
Postman output
Newman output
Steps to reproduce
Related Jira ticket

Security: Do not include passwords, API keys, database credentials, or other sensitive information when sharing logs.

29. Troubleshooting Complete

After applying a fix, verify the complete flow:

Fix Problem
    |
    v
Restart Application
    |
    v
Check Startup Logs
    |
    v
Verify SQL Server
    |
    v
Test Employee API
    |
    v
Test Account API
    |
    v
Run Postman Tests
    |
    v
Run Newman Tests
    |
    v
Confirm Successful Response

The issue should be considered resolved only after the affected API operation works successfully and the relevant Postman/Newman tests pass.

Troubleshooting Checklist
[ ] Error message identified
[ ] Spring Boot logs checked
[ ] application.yml verified
[ ] Java 21 verified
[ ] Maven verified
[ ] SQL Server verified
[ ] Database verified
[ ] Redis verified, if applicable
[ ] Port 8089 verified
[ ] API URL verified
[ ] Request body verified
[ ] Postman environment verified
[ ] Postman tests verified
[ ] Newman tests verified
[ ] HikariCP configuration checked
[ ] Database records verified
[ ] Sensitive information removed from logs
[ ] Issue retested successfully