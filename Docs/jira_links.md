Table of Contents
Purpose
Jira Project Information
Jira Ticket to Documentation Mapping
Jira Ticket Link Format
Employee API Documentation Links
Account API Documentation Links
Banking Transaction Documentation Links
Database Documentation Links
Architecture Documentation Links
Validation Documentation Links
Exception Handling Documentation Links
HikariCP Documentation Links
Actuator Monitoring Documentation Links
Environment Setup Documentation Links
Deployment Documentation Links
Troubleshooting Documentation Links
FAQ Documentation Links
Change Log Integration
Documentation Update Checklist
Jira Traceability Flow
Adding Documentation to a Jira Ticket
Adding a Jira Ticket to Documentation
Documentation Repository Structure
Jira Documentation Maintenance Rule
Jira Traceability Summary
Purpose

This document maintains traceability between Jira work items and the technical documentation of the Employee Banking REST API project.

It helps the team identify:

Which Jira ticket introduced a feature
Which documentation describes the feature
Which Jira ticket fixed an issue
Which documentation was updated
Which change is recorded in the project change log
Which Jira ticket is associated with database, API, monitoring, or deployment changes
Jira Project Information

Jira Project: Employee Banking REST API

Example Jira Project Key: EBR

Jira Base URL:

https://<your-company>.atlassian.net/browse/

Replace <your-company> with the actual company Jira workspace.

The Jira ticket IDs below are examples. Replace them with the actual Jira ticket IDs assigned to the project.

Jira Ticket to Documentation Mapping
Jira Ticket	Work Item	Documentation
EBR-001	Employee CRUD API	docs/API_DESIGN.md
EBR-002	Account Management API	docs/API_DESIGN.md
EBR-003	Deposit API	docs/API_DESIGN.md
EBR-004	Withdraw API	docs/API_DESIGN.md
EBR-005	Account Balance API	docs/API_DESIGN.md
EBR-006	Employee and Account Database Schema	docs/DATABASE_SCHEMA.md
EBR-007	System Architecture	docs/ARCHITECTURE.md
EBR-008	Validation Implementation	docs/API_DESIGN.md
EBR-009	Global Exception Handling	docs/API_DESIGN.md
EBR-010	HikariCP Configuration	docs/ENVIRONMENT_SETUP.md
EBR-011	Actuator Monitoring	docs/RUNBOOK.md
EBR-012	Environment Configuration	docs/ENVIRONMENT_SETUP.md
EBR-013	Deployment Procedure	docs/DEPLOYMENT.md
EBR-014	Troubleshooting Guide	docs/TROUBLESHOOTING.md
EBR-015	FAQ Documentation	docs/FAQ.md
EBR-016	Change Log Maintenance	CHANGELOG.md
EBR-017	Jira Documentation Traceability	docs/JIRA_LINKS.md
Jira Ticket Link Format

When an actual Jira ticket exists, use:

https://<your-company>.atlassian.net/browse/EBR-001

For example:

https://<your-company>.atlassian.net/browse/EBR-001

The placeholder Jira project key and ticket numbers must be replaced with the actual values used by the team.

Employee API Documentation Links

Employee API-related Jira tickets should reference:

docs/API_DESIGN.md

Examples of Employee API work include:

Create Employee
Get All Employees
Get Employee By ID
Update Employee
Delete Employee
Pagination
Employee Validation
Duplicate Email Handling
Example Traceability
Employee CRUD Requirement
        |
        v
Jira: EBR-001
        |
        v
Implementation
        |
        v
API Testing
        |
        v
docs/API_DESIGN.md
Account API Documentation Links

Account-related Jira tickets should reference:

docs/API_DESIGN.md

The account functionality includes:

Deposit
Withdraw
Get Balance
Example
Account Management
       |
       v
Jira: EBR-002
       |
       v
Account API
       |
       v
docs/API_DESIGN.md
Banking Transaction Documentation Links

Deposit and withdrawal functionality should be documented against the relevant Jira tickets.

Deposit
POST /api/v1/accounts/{id}/deposit

Documentation:

docs/API_DESIGN.md
Withdraw
POST /api/v1/accounts/{id}/withdraw

Documentation:

docs/API_DESIGN.md

Withdrawal-related business rules should also be documented in the API documentation and troubleshooting guide.

Database Documentation Links

Database-related Jira tickets should reference:

docs/DATABASE_SCHEMA.md

Database changes include:

Employee table changes
Account table changes
Employee-account relationships
Primary keys
Foreign keys
Constraints
Indexes
Column changes
JPA entity mapping changes
Example
Database Schema Change
        |
        v
Jira: EBR-006
        |
        v
Entity / Database Change
        |
        v
docs/DATABASE_SCHEMA.md
Architecture Documentation Links

Architecture-related Jira tickets should reference:

docs/ARCHITECTURE.md

The architecture documentation covers:

Client
   |
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
JPA / Hibernate
   |
   v
SQL Server
Example
Architecture Documentation
          |
          v
Jira: EBR-007
          |
          v
docs/ARCHITECTURE.md
Validation Documentation Links

Validation-related Jira tickets should reference:

docs/API_DESIGN.md

The project validation includes rules such as:

@NotBlank
@NotNull
@Email
@Positive

Validation-related work can include:

Required field validation
Email validation
Salary validation
Account amount validation
Invalid request handling
Example
Validation Requirement
        |
        v
Jira: EBR-008
        |
        v
Implementation
        |
        v
docs/API_DESIGN.md
Exception Handling Documentation Links

Exception handling tickets should reference:

docs/API_DESIGN.md
docs/TROUBLESHOOTING.md

The application handles errors such as:

ResourceNotFoundException
MethodArgumentNotValidException
DuplicateEmailException
BusinessException
Generic Exception

The documentation should explain the expected HTTP response for each major error.

HikariCP Documentation Links

HikariCP configuration-related Jira tickets should reference:

docs/ENVIRONMENT_SETUP.md
docs/RUNBOOK.md

The documentation should cover:

maximum-pool-size
minimum-idle
connection-timeout
max-lifetime
Pool monitoring
Example
HikariCP Configuration
        |
        v
Jira: EBR-010
        |
        +---- docs/ENVIRONMENT_SETUP.md
        |
        +---- docs/RUNBOOK.md
Actuator Monitoring Documentation Links

Spring Boot Actuator-related Jira tickets should reference:

docs/RUNBOOK.md
docs/ENVIRONMENT_SETUP.md

Monitoring includes:

/actuator/health
/actuator/metrics
/actuator/info

Metrics may include:

JVM memory
JVM threads
HTTP requests
HikariCP connections
Disk usage
Application startup time
Example
Actuator Monitoring
       |
       v
Jira: EBR-011
       |
       +---- docs/RUNBOOK.md
       |
       +---- docs/ENVIRONMENT_SETUP.md
Environment Setup Documentation Links

Environment-related Jira tickets should reference:

docs/ENVIRONMENT_SETUP.md

Environment setup includes:

Java 21
Maven
Eclipse/IDE
SQL Server
Database configuration
JDBC configuration
HikariCP configuration
Application configuration
Actuator configuration
Example
Environment Setup
       |
       v
Jira: EBR-012
       |
       v
docs/ENVIRONMENT_SETUP.md
Deployment Documentation Links

Deployment-related Jira tickets should reference:

docs/DEPLOYMENT.md

Deployment documentation covers:

Maven Build
     |
     v
JAR Creation
     |
     v
Environment Configuration
     |
     v
Application Startup
     |
     v
Database Verification
     |
     v
API Verification
Example
Deployment Procedure
       |
       v
Jira: EBR-013
       |
       v
docs/DEPLOYMENT.md
Troubleshooting Documentation Links

Bug-fix Jira tickets should reference:

docs/TROUBLESHOOTING.md

when the issue is likely to occur again.

Common troubleshooting topics include:

SQL Server connection failure
Integrated authentication error
HikariCP connection pool exhaustion
400 Bad Request
404 Not Found
Insufficient balance
Duplicate email
Application startup failure
Actuator health failure
Port already in use
Example
Problem
   |
   v
Jira: EBR-014
   |
   v
Bug Fix
   |
   v
docs/TROUBLESHOOTING.md
FAQ Documentation Links

Frequently asked project questions should be maintained in:

docs/FAQ.md

FAQ updates can be associated with:

Jira: EBR-015

Examples include:

How to start the application
How to create an employee
How to perform a deposit
How to perform a withdrawal
Why withdrawal fails
How to check account balance
How to check Actuator health
How to troubleshoot database errors
Change Log Integration

Significant Jira work should also be recorded in:

CHANGELOG.md

Example:

## [1.1.0] - 2026-08-12

### Added

- Added Account Deposit API.
- Added Account Withdrawal API.
- Added Account Balance API.
- Jira: EBR-003
- Jira: EBR-004
- Jira: EBR-005

This provides traceability between:

Jira Ticket
     |
     v
Code Change
     |
     v
Testing
     |
     v
Documentation
     |
     v
CHANGELOG.md
Documentation Update Checklist

When completing a Jira ticket, verify:

 Requirement completed
 Code implementation completed
 Unit/integration testing completed
 API testing completed
 Relevant documentation updated
 Jira ticket referenced
 Documentation link added to Jira
 CHANGELOG.md updated if required
 Troubleshooting guide updated if required
 Jira ticket moved to the appropriate status
Jira Traceability Flow

The standard project documentation process is:

Jira Requirement
       |
       v
Implementation
       |
       v
Testing
       |
       v
Documentation Update
       |
       +--------------------+
       |         |          |
       v         v          v
 API_DESIGN  DATABASE    RUNBOOK
             SCHEMA
       |         |          |
       +---------+----------+
                 |
                 v
           CHANGELOG.md
Adding Documentation to a Jira Ticket

When a Jira ticket is completed, add the relevant documentation to the ticket.

Example Jira Comment
Implementation completed.

Documentation updated:

- docs/API_DESIGN.md
- docs/DATABASE_SCHEMA.md
- docs/TROUBLESHOOTING.md

Change log:
- CHANGELOG.md

For deployment-related work:

Implementation completed.

Documentation updated:

- docs/DEPLOYMENT.md
- docs/ENVIRONMENT_SETUP.md

Change log:
- CHANGELOG.md
Adding a Jira Ticket to Documentation

When documentation is updated because of a Jira ticket, record the ticket ID.

Example:

Related Jira: EBR-003

For multiple related tickets:

Related Jira:
- EBR-003
- EBR-004
- EBR-005

This makes it possible to trace the documentation back to the original requirement.

Documentation Repository Structure

The project's documentation should be maintained using the following structure:

EmployeeBankRestApi/
│
├── src/
│
├── pom.xml
├── CHANGELOG.md
│
└── docs/
    ├── API_DESIGN.md
    ├── ARCHITECTURE.md
    ├── DATABASE_SCHEMA.md
    ├── RUNBOOK.md
    ├── DEPLOYMENT.md
    ├── ENVIRONMENT_SETUP.md
    ├── TROUBLESHOOTING.md
    ├── FAQ.md
    └── JIRA_LINKS.md
Jira Documentation Maintenance Rule

Whenever a new feature, bug fix, database change, configuration change, monitoring change, or deployment change is implemented:

Create / Update Jira Ticket
            |
            v
Implement Change
            |
            v
Test Change
            |
            v
Update Relevant Documentation
            |
            v
Update JIRA_LINKS.md
            |
            v
Update CHANGELOG.md

The documentation must be updated whenever the implemented behavior changes.

Jira Traceability Summary

The following documentation mapping should be maintained throughout the project:

Area	Documentation
Employee APIs	docs/API_DESIGN.md
Account APIs	docs/API_DESIGN.md
Deposit / Withdrawal / Balance	docs/API_DESIGN.md
Validation	docs/API_DESIGN.md
Exception Handling	docs/API_DESIGN.md
System Architecture	docs/ARCHITECTURE.md
Employee / Account Database	docs/DATABASE_SCHEMA.md
HikariCP / Monitoring	docs/RUNBOOK.md
Actuator	docs/RUNBOOK.md
Environment Setup	docs/ENVIRONMENT_SETUP.md
Deployment	docs/DEPLOYMENT.md
Troubleshooting	docs/TROUBLESHOOTING.md
Frequently Asked Questions	docs/FAQ.md
Change History	CHANGELOG.md
Jira Traceability	docs/JIRA_LINKS.md