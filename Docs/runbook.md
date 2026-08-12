Employee & Account API - Operations Runbook
1. Purpose

This runbook provides instructions for common operational tasks for the Employee & Account REST API.

It covers:

Starting the application
Stopping the application
Verifying application availability
Checking the SQL Server database
Testing Employee APIs
Testing Account APIs
Running Postman collections
Checking application logs
Handling common errors
Restart and recovery procedures
2. Application Information
Property	Value
Application	Employee API
Framework	Spring Boot
Language	Java 21
Build Tool	Maven
Database	Microsoft SQL Server
Persistence	Spring Data JPA / Hibernate
API Testing	Postman
CLI Testing	Newman
Default Port	8089
API Base Path	/api/v1

The application contains two major functional areas:

Employee Management
        +
Account / Banking Management
3. Prerequisites

Before starting the application, verify that the following are available:

Java 21
Maven
Microsoft SQL Server
SQL Server Management Studio
Postman
Node.js
Newman

The SQL Server database must be available and the configured database must exist.

4. Start the Application
Using Eclipse

Open the project in Eclipse.

Locate the main Spring Boot class, for example:

EmployeeApiApplication.java

Right-click:

Run As
   ↓
Spring Boot App

Wait until the console displays a successful startup message similar to:

Started EmployeeApiApplication

The application should then be available on:

http://localhost:8089
5. Start Using Maven

Open Command Prompt or terminal in the project root directory.

Run:

mvn spring-boot:run

Wait for Spring Boot to complete startup.

A successful startup should display a message similar to:

Started EmployeeApiApplication
6. Stop the Application
Eclipse

Click the red:

Terminate

button in the Eclipse Console.

Command Line

If the application is running in the terminal, press:

Ctrl + C
7. Verify Application Availability

The application exposes Employee and Account APIs under:

/api/v1

Verify the Employee API:

GET http://localhost:8089/api/v1/employees

Expected:

200 OK

The Account API can also be verified using:

GET http://localhost:8089/api/v1/accounts/{id}/balance

If the account exists, the API should return:

200 OK

If the account does not exist:

404 Not Found
8. Verify SQL Server

Open SQL Server Management Studio and connect to the SQL Server instance configured in:

src/main/resources/application.yml

Verify the configured database.

Then execute:

SELECT DB_NAME() AS CurrentDatabase;

Verify that the application is connected to the expected database.

9. Check Database Tables

The project contains the Employee and Account domains.

Verify the available tables:

SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE';

Check the tables associated with:

Employee
Account

The exact table names should match the @Table mappings in the entity classes.

10. Check Employee Records

Use SQL Server Management Studio to verify employee data.

For example:

SELECT *
FROM employees;

Use the actual table name configured in Employee.java if it differs from employees.

To verify a particular employee:

SELECT *
FROM employees
WHERE id = 1;
11. Check Account Records

Verify account data using:

SELECT *
FROM accounts;

To verify a particular account:

SELECT *
FROM accounts
WHERE id = 1;

The exact table name should match the mapping configured in Account.java.

12. Test Employee APIs Using Postman

Open Postman and select the Employee API collection/environment configured for the project.

The main Employee operations are:

GET    /api/v1/employees
GET    /api/v1/employees/{id}
POST   /api/v1/employees
PUT    /api/v1/employees/{id}
DELETE /api/v1/employees/{id}

Example:

GET http://localhost:8089/api/v1/employees

Expected:

200 OK
13. Test Account APIs Using Postman

The Account APIs provide banking operations.

Deposit
POST /api/v1/accounts/{id}/deposit
Withdraw
POST /api/v1/accounts/{id}/withdraw
Balance
GET /api/v1/accounts/{id}/balance

Example:

GET http://localhost:8089/api/v1/accounts/1/balance

Expected:

200 OK

when the account exists.

14. Account Transaction Verification

For a deposit:

Account
   |
   v
Deposit Request
   |
   v
Account Service
   |
   v
Update Balance
   |
   v
Repository
   |
   v
SQL Server

For a withdrawal:

Withdrawal Request
        |
        v
Check Account
        |
        v
Check Available Balance
        |
        +---- Insufficient Balance
        |          |
        |          v
        |      Error Response
        |
        +---- Sufficient Balance
                   |
                   v
             Update Balance
                   |
                   v
               SQL Server
15. Verify Employee Pagination

The Employee API supports paginated retrieval.

Example:

GET /api/v1/employees?page=0&size=10

The response should contain the employees for the requested page.

For sorting:

GET /api/v1/employees?page=0&size=10&sort=name

Use the actual entity property name when specifying sorting.

16. Expected HTTP Status Codes
Status	Meaning
200 OK	Successful GET, PUT, deposit or valid withdrawal
201 Created	Employee/account resource successfully created
204 No Content	Successful DELETE
400 Bad Request	Invalid request or validation/business input error
404 Not Found	Requested employee/account does not exist
409 Conflict	Duplicate resource, such as duplicate email
422 Unprocessable Entity	Business rule violation
500 Internal Server Error	Unexpected application error
17. Check Application Logs

Application logs are displayed in the Eclipse console or terminal.

The project uses SLF4J/Logback logging.

Look for:

Started EmployeeApiApplication

for successful startup.

Application logs can also show:

HTTP requests
HTTP responses
Employee operations
Account operations
Validation failures
Exceptions
Database-related errors

Correlation IDs/MDC values can be used to trace requests when configured.

Do not expose passwords, authentication secrets, or other sensitive information in logs.

18. Application Fails to Start

If the application fails during startup, check the console for the root exception.

Common causes include:

SQL Server not running
Incorrect datasource URL
Database does not exist
Integrated authentication problem
JDBC driver problem
Port 8089 already in use
Invalid application.yml configuration
Missing dependency

First verify:

java -version

Then verify SQL Server and the datasource configuration.

19. Port 8089 Already in Use

Check whether another process is using port 8089.

On Windows:

netstat -ano | findstr :8089

The command displays the PID using the port.

If the process is no longer required, it can be terminated:

taskkill /PID <PID> /F

Then restart the Spring Boot application.

20. SQL Server Connection Failure

If the application cannot connect to SQL Server, verify:

[ ] SQL Server service is running
[ ] Correct server/instance is configured
[ ] Database exists
[ ] JDBC URL is correct
[ ] SQL Server JDBC driver is available
[ ] Integrated authentication is configured correctly
[ ] Windows account has database access

For integrated authentication problems, also verify that the required Microsoft SQL Server JDBC authentication component is correctly configured for the Java/JDBC driver version being used.

Restart the application after correcting the configuration.

21. API Returns 400, 404, 409 or 422
400 Bad Request

Check the request body and validation fields.

For example:

Required field missing
Invalid email
Invalid salary
Invalid request value
404 Not Found

Verify that the requested employee or account ID exists.

SELECT *
FROM employees
WHERE id = 1;

or:

SELECT *
FROM accounts
WHERE id = 1;
409 Conflict

A duplicate resource may already exist.

For example:

Duplicate employee email

Check the existing database records.

422 Unprocessable Entity

The request may violate a business rule.

For example:

Insufficient account balance

Check the account's current balance before retrying the transaction.

22. Postman Collection Fails

If the Postman tests fail, verify:

[ ] Spring Boot application is running
[ ] Correct Postman environment is selected
[ ] baseUrl points to http://localhost:8089
[ ] Correct endpoint is being used
[ ] Request body is valid
[ ] Required IDs exist
[ ] Database is available

For Employee tests, verify that the employee data used by subsequent requests was successfully created.

For Account tests, verify that the account exists and has sufficient balance for withdrawal operations.

23. Run Newman Tests

Verify Newman:

newman --version

Run the exported Postman collection:

newman run "Employee API.postman_collection.json" -e "Employee API Local.postman_environment.json"

If the environment is not configured correctly, provide the local base URL explicitly:

newman run "Employee API.postman_collection.json" -e "Employee API Local.postman_environment.json" --env-var "baseUrl=http://localhost:8089"

The exact collection/environment filenames should match the exported project files.

24. Restart Procedure

For a routine restart:

1. Stop Spring Boot
        |
        v
2. Verify SQL Server is running
        |
        v
3. Verify application.yml
        |
        v
4. Start Spring Boot
        |
        v
5. Wait for successful startup
        |
        v
6. Test Employee API
        |
        v
7. Test Account API
        |
        v
8. Confirm successful responses

Example verification:

GET http://localhost:8089/api/v1/employees

Then:

GET http://localhost:8089/api/v1/accounts/1/balance
25. Basic Recovery Checklist

If the application is not working:

[ ] Check Java version
[ ] Check Maven
[ ] Check SQL Server
[ ] Check database availability
[ ] Check application.yml
[ ] Check JDBC configuration
[ ] Check port 8089
[ ] Check Spring Boot console
[ ] Restart the application
[ ] Test Employee API
[ ] Test Account API
[ ] Run Postman collection
[ ] Run Newman if required

If the problem continues, capture:

Error message
HTTP method
API endpoint
Request body
HTTP status
Relevant application logs
Database error, if any

and associate the issue with the relevant Jira ticket.