Employee REST API — API Design Document
1. Overview

The Employee REST API is a Spring Boot REST application used to manage employee information.

The API provides CRUD operations for employees and supports:

Creating employees
Retrieving employees
Retrieving an employee by ID
Updating employee information
Deleting employees
Pagination and sorting
Request validation
Duplicate email validation
Standard HTTP status codes
Consistent error responses

The API is part of the EmployeeBankRestApiMonitoring_Actuator application.

2. Base URL

Local development base URL:

http://localhost:8089

API base path:

/api/v1

Employee API base path:

/api/v1/employees
3. Content Type

Requests containing a request body use:

Content-Type: application/json

Responses are returned in JSON format.

DELETE operations return no response body when the operation is successful.

4. Employee Endpoints
4.1 Create Employee

Creates a new employee.

Endpoint
POST /api/v1/employees
Request Body
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "salary": 50000
}
Validation

The employee request is validated before processing.

Typical validation rules include:

name must not be blank.
email must not be blank.
email must have a valid email format.
salary must satisfy the configured validation rules.
Employee email must be unique.
Success Response

Status: 201 Created

{
  "id": 1,
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "salary": 50000
}
Error Responses

400 Bad Request

Returned when the request contains invalid data.

{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "name": "Name must not be blank",
    "email": "Email must be valid"
  },
  "path": "/api/v1/employees"
}

409 Conflict

Returned when an employee already exists with the supplied email.

{
  "status": 409,
  "message": "Employee with email already exists",
  "path": "/api/v1/employees"
}
5. Get All Employees

Returns a paginated list of employees.

Endpoint
GET /api/v1/employees
Query Parameters
Parameter	Type	Required	Description
page	Integer	No	Page number, starting from 0
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
      "name": "Rahul Sharma",
      "email": "rahul@example.com",
      "salary": 50000
    },
    {
      "id": 2,
      "name": "Priya Das",
      "email": "priya@example.com",
      "salary": 60000
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 2,
  "totalPages": 1
}

The exact pagination metadata depends on the Page<Employee> response returned by the application's controller.

If no employees are available, the API returns an empty page.

6. Get Employee By ID

Returns an employee using its unique ID.

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
  "salary": 50000
}
Error Response

Status: 404 Not Found

Returned when the requested employee does not exist.

{
  "status": 404,
  "message": "Employee not found",
  "path": "/api/v1/employees/100"
}
7. Update Employee

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
  "salary": 65000
}
Success Response

Status: 200 OK

{
  "id": 1,
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "salary": 65000
}
Error Responses

400 Bad Request

Returned when employee information fails validation.

404 Not Found

Returned when the employee ID does not exist.

409 Conflict

Returned when the updated email conflicts with an existing employee email.

8. Delete Employee

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

The response does not contain a response body.

Error Response

Status: 404 Not Found

Returned when the employee does not exist.

9. Validation and Business Rules

The Employee REST API applies validation and business rules before processing requests.

Validation

The API validates:

Required employee fields
Email format
Salary values
Request data format

Invalid input returns:

400 Bad Request
Duplicate Email

Employee email addresses must be unique.

Attempting to create or update an employee with an existing email returns:

409 Conflict
Resource Not Found

If an employee ID does not exist:

404 Not Found
Business Rule Violation

If a request violates an application-specific business rule:

422 Unprocessable Entity
10. Error Response Format

The application uses a consistent error response structure for API errors.

Example:

{
  "timestamp": "2026-08-12T10:30:00",
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Email must be valid"
  },
  "path": "/api/v1/employees"
}

The error response can contain:

Field	Description
timestamp	Time when the error occurred
status	HTTP status code
message	Error description
errors	Detailed validation errors
path	API endpoint that generated the error
11. HTTP Status Codes
Status	Meaning	Usage
200	OK	Successful GET and PUT
201	Created	Successful employee creation
204	No Content	Successful employee deletion
400	Bad Request	Invalid request or validation failure
404	Not Found	Employee does not exist
409	Conflict	Duplicate employee email
422	Unprocessable Entity	Business rule violation
500	Internal Server Error	Unexpected server-side error
12. Request Flow
Client / Postman
       |
       v
Employee Controller
       |
       v
Employee Service
       |
       v
Employee Repository
       |
       v
SQL Server Database

The request flow is:

The client sends an HTTP request.
The Controller receives the request.
Request data is validated.
The Service layer executes business logic.
The Repository communicates with SQL Server.
The result is returned through the Service and Controller.
The Controller sends the HTTP response to the client.
13. API Architecture

The Employee API follows a layered Spring Boot architecture.

+----------------------+
|       Client         |
|      / Postman       |
+----------+-----------+
           |
           v
+----------------------+
|   REST Controller    |
+----------+-----------+
           |
           v
+----------------------+
|    Service Layer     |
+----------+-----------+
           |
           v
+----------------------+
|  Repository Layer    |
+----------+-----------+
           |
           v
+----------------------+
|    SQL Server DB     |
+----------------------+

Additional application components include:

                    +------------------+
                    |     Actuator     |
                    | Health & Metrics |
                    +--------+---------+
                             |
                             v
Client --> Controller --> Service --> Repository --> SQL Server
                             |
                             v
                    Exception Handling
14. Postman Collection

The Employee API endpoints are maintained in the:

EmployeeRestApiPostmanCollection

Postman collection.

The collection contains requests for:

EmployeeRestApiPostmanCollection
│
├── Create Employee
├── Get All Employees
├── Get Employee By ID
├── Update Employee
├── Delete Employee
│
└── Negative Test Cases
    ├── Invalid Employee
    ├── Employee Not Found
    └── Duplicate Email
15. Postman Environment Variables

The Postman collection can use environment variables to avoid hardcoding configuration values.

Example:

baseUrl = http://localhost:8089

API requests can then be written as:

{{baseUrl}}/api/v1/employees

For employee-specific operations:

{{baseUrl}}/api/v1/employees/{{employeeId}}

Example environment variables:

Variable	Example Value	Purpose
baseUrl	http://localhost:8089	Application base URL
employeeId	1	Employee identifier
16. Postman Test Coverage

The Postman collection should validate the following scenarios:

Test Case	Expected Status
Create valid employee	201
Create employee with invalid data	400
Create employee with duplicate email	409
Get all employees	200
Get existing employee	200
Get non-existing employee	404
Update existing employee	200
Update non-existing employee	404
Update with invalid data	400
Delete existing employee	204
Delete non-existing employee	404
17. API Versioning

The Employee API uses versioning through the URL:

/api/v1

Therefore, the current employee API endpoints are:

/api/v1/employees
/api/v1/employees/{id}

Versioning allows future API versions to be introduced without breaking existing clients.

18. API Documentation Summary

The Employee REST API provides the following operations:

Operation	Method	Endpoint	Response
Create Employee	POST	/api/v1/employees	201
Get All Employees	GET	/api/v1/employees	200
Get Employee	GET	/api/v1/employees/{id}	200 / 404
Update Employee	PUT	/api/v1/employees/{id}	200 / 404
Delete Employee	DELETE	/api/v1/employees/{id}	204 / 404