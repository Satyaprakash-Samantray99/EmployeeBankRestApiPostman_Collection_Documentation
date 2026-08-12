# EmployeeBankRestApi - API Design Document

## 1. Overview

The EmployeeBankRestApi is a Spring Boot REST application used to manage employees and bank accounts.

The API provides CRUD operations for:

- Employees
- Bank Accounts
- Deposit operations
- Withdrawal operations
- Account balance inquiry

Employees and accounts are managed using Spring Data JPA and Hibernate.

The application uses Microsoft SQL Server for persistent data storage.

---

## 2. Base URL

Local development base URL:

```text
http://localhost:8089

API base path:

/api/v1
3. Content Type

Requests containing a body use:

Content-Type: application/json

Responses are returned in JSON format except DELETE operations, which return no response body.

4. Employee Endpoints
4.1 Create Employee

Creates a new employee.

Endpoint
POST /api/v1/employees
Request Body
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "designation": "Software Engineer",
  "salary": 50000
}
Validation
name must not be blank.
email must not be blank.
email must be a valid email address.
designation must not be blank.
salary is required.
salary must be greater than zero.
Employee email must be unique.
Success Response

Status: 201 Created

{
  "id": 1,
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "designation": "Software Engineer",
  "salary": 50000
}
Error Responses
400 Bad Request for validation errors.
409 Conflict if the employee email already exists.
4.2 Get All Employees

Returns all employees with pagination support.

Endpoint
GET /api/v1/employees
Success Response

Status: 200 OK

[
  {
    "id": 1,
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "designation": "Software Engineer",
    "salary": 50000
  }
]

If no employees exist:

[]

Pagination and sorting parameters can be supplied according to the configured Spring Data Pageable implementation.

4.3 Get Employee By ID

Returns an employee using its ID.

Endpoint
GET /api/v1/employees/{id}
Path Parameter
Parameter	Type	Description
id	Long	Employee ID
Example
GET /api/v1/employees/1
Success Response

Status: 200 OK

{
  "id": 1,
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "designation": "Software Engineer",
  "salary": 50000
}
Error Response

Status: 404 Not Found

{
  "timestamp": "2026-08-12T10:30:00",
  "status": 404,
  "message": "Employee not found",
  "errors": null,
  "path": "/api/v1/employees/100"
}
4.4 Update Employee

Updates an existing employee.

Endpoint
PUT /api/v1/employees/{id}
Path Parameter
Parameter	Type	Description
id	Long	Employee ID
Request Body
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "designation": "Senior Software Engineer",
  "salary": 65000
}
Success Response

Status: 200 OK

{
  "id": 1,
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "designation": "Senior Software Engineer",
  "salary": 65000
}
Error Responses
400 Bad Request for validation errors.
404 Not Found if the employee does not exist.
409 Conflict if the email conflicts with another employee.
4.5 Delete Employee

Deletes an existing employee.

Endpoint
DELETE /api/v1/employees/{id}
Path Parameter
Parameter	Type	Description
id	Long	Employee ID
Example
DELETE /api/v1/employees/1
Success Response

Status: 204 No Content

The response does not contain a body.

Error Response

Returns 404 Not Found if the employee does not exist.

5. Account Endpoints
5.1 Deposit Money

Deposits money into an existing bank account.

Endpoint
POST /api/v1/accounts/{id}/deposit
Path Parameter
Parameter	Type	Description
id	Long	Account ID
Example
POST /api/v1/accounts/1/deposit
Request Body

The request body should contain the deposit amount according to the Account API implementation.

Example:

{
  "amount": 1000
}
Validation
Account ID must identify an existing account.
Deposit amount is required.
Deposit amount must satisfy the configured validation rules.
Invalid amounts are rejected.
Success Response

Status: 200 OK

The account balance is updated with the deposited amount.

Error Response

Returns an appropriate error response if the account does not exist or the request fails validation.

5.2 Withdraw Money

Withdraws money from an existing bank account.

Endpoint
POST /api/v1/accounts/{id}/withdraw
Path Parameter
Parameter	Type	Description
id	Long	Account ID
Example
POST /api/v1/accounts/1/withdraw
Request Body
{
  "amount": 500
}
Validation
Account ID must identify an existing account.
Withdrawal amount is required.
Withdrawal amount must satisfy the configured validation rules.
Withdrawal amount cannot exceed the available balance.
Success Response

Status: 200 OK

The account balance is reduced by the withdrawal amount.

Error Response

Status: 400 Bad Request

If the account does not have sufficient balance, the withdrawal is rejected.

Example message:

Insufficient balance.
5.3 Get Account Balance

Returns the current balance of an account.

Endpoint
GET /api/v1/accounts/{id}/balance
Path Parameter
Parameter	Type	Description
id	Long	Account ID
Example
GET /api/v1/accounts/1/balance
Success Response

Status: 200 OK

The response contains the current account balance according to the Account API implementation.

Example:

{
  "id": 1,
  "balance": 5000
}
Error Response

Status: 404 Not Found

Returned when the requested account does not exist.

6. HTTP Status Codes
Status	Meaning	Usage
200	OK	Successful GET, PUT, deposit, withdrawal, and balance operations
201	Created	Successful employee creation
204	No Content	Successful employee deletion
400	Bad Request	Request validation failure or insufficient balance
404	Not Found	Employee or account does not exist
409	Conflict	Duplicate employee email
422	Unprocessable Entity	Business rule violation where configured
500	Internal Server Error	Unexpected server-side failure
7. Request Flow
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
SQL Server Database

The controller receives the HTTP request, the service handles business logic, the repository performs database operations, and the response is returned to the client.

For account transactions, the service layer also validates banking business rules such as insufficient balance.

8. Postman Testing

The endpoints are maintained in the:

EmployeeBankRestApi

Postman collection.

The collection contains modules for:

Employee APIs
Account APIs
Deposit
Withdrawal
Account Balance

Environment variables include:

baseUrl
employeeId
accountId

For local execution:

baseUrl = http://localhost:8089

Employee-specific requests can use:

{{baseUrl}}/api/v1/employees/{{employeeId}}

Account-specific requests can use:

{{baseUrl}}/api/v1/accounts/{{accountId}}/balance

Automated tests can validate:

HTTP status codes
Response bodies
Employee creation
Employee retrieval
Employee updates
Employee deletion
Deposit operations
Withdrawal operations
Account balance
Validation errors
Insufficient balance
Duplicate email handling

The Postman collection can also be executed through Newman CLI.

Example:

newman run "EmployeeBankRestApiPostmanCollection.postman_collection.json" -e "EmployeeBankRestApi.postman_environment.json"

A successful test execution should complete without failed requests or failed assertions.