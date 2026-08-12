EmployeeBankRestApi - API Design Document
1. Overview

The EmployeeBankRestApi is a Spring Boot REST application that provides RESTful APIs for managing employees and bank accounts.

The application contains two primary modules:

Employee Management
Account Management

The Employee module provides CRUD operations for employee records, while the Account module provides banking operations such as depositing money, withdrawing money, and checking account balance.

The APIs are designed to be tested using Postman.

2. Base URL

Local development base URL:

http://localhost:8089

API base path:

/api/v1
3. Content Type

Requests containing a body use:

Content-Type: application/json

API responses are returned in JSON format.

4. Employee APIs
4.1 Get All Employees

Retrieves a paginated list of employees.

Endpoint
GET /api/v1/employees
Query Parameters
Parameter	Type	Required	Description
page	Integer	No	Page number
size	Integer	No	Number of records per page
sort	String	No	Field used for sorting
Example
GET /api/v1/employees?page=0&size=10&sort=name
Success Response

Status: 200 OK

Example:

{
  "content": [
    {
      "id": 1,
      "name": "Satyaprakash Samantray",
      "email": "satyaprakash@gmail.com",
      "department": "IT",
      "salary": 55000
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
4.2 Get Employee By ID

Retrieves a specific employee using the employee ID.

Endpoint
GET /api/v1/employees/{id}
Path Parameter
Parameter	Type	Description
id	Integer	Employee ID
Example
GET /api/v1/employees/1
Success Response

Status: 200 OK

{
  "id": 1,
  "name": "Satyaprakash Samantray",
  "email": "satyaprakash@gmail.com",
  "department": "IT",
  "salary": 55000
}
Error Response

Status: 404 Not Found

Returned when the employee does not exist.

5. Create Employee

Creates a new employee.

Endpoint
POST /api/v1/employees
Request Body
{
  "name": "Satyaprakash Samantray",
  "email": "satyaprakash@gmail.com",
  "department": "IT",
  "salary": 55000
}
Success Response

Status: 201 Created

Returns the newly created employee.

Validation

The request is validated before creating the employee.

Possible validation failure:

400 Bad Request

If the email already exists:

409 Conflict

If the salary violates the application business rule:

422 Unprocessable Entity
6. Update Employee

Updates an existing employee.

Endpoint
PUT /api/v1/employees/{id}
Request Body
{
  "name": "Satyaprakash Samantray",
  "email": "satyaprakash@gmail.com",
  "department": "Development",
  "salary": 65000
}
Success Response

Status: 200 OK

Error Responses
Status	Description
400 Bad Request	Validation failure
404 Not Found	Employee does not exist
409 Conflict	Duplicate email
422 Unprocessable Entity	Business rule violation
7. Delete Employee

Deletes an employee.

Endpoint
DELETE /api/v1/employees/{id}
Success Response

Status: 204 No Content

No response body is returned.

Error Response

Status: 404 Not Found

Returned when the employee does not exist.

8. Account APIs

The Account module provides banking operations for employee accounts.

The main operations are:

Deposit money
Withdraw money
Check account balance
9. Deposit Money

Deposits money into an account.

Endpoint
POST /api/v1/accounts/{id}/deposit
Path Parameter
Parameter	Type	Description
id	Integer	Account ID
Request Body
{
  "amount": 5000.00
}
Success Response

Status: 200 OK

Example:

{
  "accountId": 1,
  "accountNumber": "ACC1001",
  "accountHolderName": "Satya Samantray",
  "balance": 15000.00
}
10. Withdraw Money

Withdraws money from an account.

Endpoint
POST /api/v1/accounts/{id}/withdraw
Path Parameter
Parameter	Type	Description
id	Integer	Account ID
Request Body
{
  "amount": 5000.00
}
Success Response

Status: 200 OK

Example:

{
  "accountId": 1,
  "accountNumber": "ACC1001",
  "accountHolderName": "Satya Samantray",
  "balance": 10000.00
}
Business Rule

The account must have sufficient balance for the withdrawal.

If the account does not have sufficient balance, the application throws a business exception.

Example error:

{
  "status": 400,
  "message": "Insufficient balance."
}
11. Get Account Balance

Retrieves the current balance of an account.

Endpoint
GET /api/v1/accounts/{id}/balance
Path Parameter
Parameter	Type	Description
id	Integer	Account ID
Example
GET /api/v1/accounts/1/balance
Success Response

Status: 200 OK

Example:

{
  "balance": 25000.00
}
Error Response

Status: 404 Not Found

Returned when the specified account does not exist.

12. HTTP Status Codes
Status	Meaning	Usage
200 OK	Request successful	GET, deposit, withdrawal, update
201 Created	Resource created	Employee creation
204 No Content	Resource deleted	Employee deletion
400 Bad Request	Invalid request	Validation/business failure
404 Not Found	Resource not found	Employee/account not found
409 Conflict	Resource conflict	Duplicate employee email
422 Unprocessable Entity	Business rule violation	Salary/business validation
500 Internal Server Error	Server error	Unexpected application error
13. Error Response

The API uses a common error response structure for API errors.

Example
{
  "timestamp": "2026-08-12T10:30:00",
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Invalid email format"
  },
  "path": "/api/v1/employees"
}
Error Response Fields
Field	Description
timestamp	Time at which the error occurred
status	HTTP status code
message	Error message
errors	Detailed validation errors
path	API endpoint
14. Request Flow

The general request flow is:

Client / Postman
       |
       v
+---------------------+
| REST Controllers     |
+----------+----------+
           |
     +-----+-----+
     |           |
     v           v
Employee      Account
Service       Service
     |           |
     v           v
Employee      Account
Repository    Repository
     |           |
     +-----+-----+
           |
           v
      SQL Server

The Controller receives the request, the Service layer performs business logic, the Repository layer communicates with the database, and the result is returned to Postman.

15. Postman Collection

The Postman collection contains requests for both Employee and Account APIs.

EmployeeBankRestApi
|
+-- Employee APIs
|   |
|   +-- Get All Employees
|   +-- Get Employee By ID
|   +-- Create Employee
|   +-- Update Employee
|   +-- Delete Employee
|
+-- Account APIs
    |
    +-- Deposit
    +-- Withdraw
    +-- Get Balance
16. Postman Environment Variables

The collection can use environment variables such as:

Variable	Example	Purpose
baseUrl	http://localhost:8089	Application URL
employeeId	1	Employee ID
accountId	1	Account ID
Base URL Example
{{baseUrl}}/api/v1/employees
Employee Request
{{baseUrl}}/api/v1/employees/{{employeeId}}
Account Request
{{baseUrl}}/api/v1/accounts/{{accountId}}/balance
17. Postman Test Coverage
Employee API Tests
Test Case	Expected Status
Get all employees	200
Get existing employee	200
Get non-existing employee	404
Create employee	201
Create employee with invalid data	400
Create duplicate email	409
Update employee	200
Update non-existing employee	404
Delete employee	204
Delete non-existing employee	404
Account API Tests
Test Case	Expected Status
Deposit amount	200
Withdraw amount	200
Withdraw with insufficient balance	400
Get account balance	200
Get non-existing account balance	404
18. API Summary
Module	Operation	Method	Endpoint	Success
Employee	Get all	GET	/api/v1/employees	200
Employee	Get by ID	GET	/api/v1/employees/{id}	200
Employee	Create	POST	/api/v1/employees	201
Employee	Update	PUT	/api/v1/employees/{id}	200
Employee	Delete	DELETE	/api/v1/employees/{id}	204
Account	Deposit	POST	/api/v1/accounts/{id}/deposit	200
Account	Withdraw	POST	/api/v1/accounts/{id}/withdraw	200
Account	Get balance	GET	/api/v1/accounts/{id}/balance	200