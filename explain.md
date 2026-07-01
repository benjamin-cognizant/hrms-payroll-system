# HRMS - Human Resource Management System Documentation

## Table of Contents

1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [How to Run the Project](#how-to-run-the-project)
5. [Database Setup](#database-setup)
6. [Module 1 - Employee Management](#module-1---employee-management)
7. [Module 2 - Recruitment Management](#module-2---recruitment-management)
8. [Module 3 - Frontend Pages](#module-3---frontend-pages)
9. [API Reference](#api-reference)
10. [How the Code Layers Work (Beginner Explanation)](#how-the-code-layers-work)
11. [Database Tables](#database-tables)
12. [Key Concepts for Beginners](#key-concepts-for-beginners)

---

## Project Overview

This is a Human Resource Management System (HRMS) built as a web application. It helps an organization manage:

- Employees (create, update, delete, view employee records)
- Recruitment (manage candidates, job openings, interviews, and offer letters)
- Payroll (view salary information)
- Login and role-based access (Employee view and HR Recruiter view)

---

## Technology Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Language   | Java 21                           |
| Framework  | Spring Boot 3.3.0                 |
| Database   | MySQL                             |
| ORM        | Spring Data JPA (Hibernate)       |
| Build Tool | Maven                             |
| Frontend   | HTML, CSS, JavaScript             |
| Template   | Thymeleaf (for some pages)        |
| Utility    | Lombok (reduces boilerplate code) |
| Validation | Jakarta Bean Validation           |

---

## Project Structure

```
hrms-payroll-system/
├── backend/
│   ├── pom.xml                        (Maven build file - lists dependencies)
│   ├── database_schema.sql            (SQL to create tables)
│   ├── src/main/java/com/hrms/
│   │   ├── employee/                  (Employee module)
│   │   │   ├── Application.java       (Main class - starts the app)
│   │   │   ├── controller/            (Handles HTTP requests)
│   │   │   │   ├── EmployeeController.java
│   │   │   │   └── LoginController.java
│   │   │   ├── model/                 (Data classes - maps to DB tables)
│   │   │   │   ├── Employee.java
│   │   │   │   └── User.java
│   │   │   ├── repository/            (Talks to the database)
│   │   │   │   ├── EmployeeRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   └── service/               (Business logic)
│   │   │       └── EmployeeService.java
│   │   └── recruiter/                 (Recruitment module)
│   │       ├── config/
│   │       │   └── WebConfig.java     (CORS and static file settings)
│   │       ├── controller/
│   │       │   └── RecruitmentRestController.java
│   │       ├── model/
│   │       │   ├── Interview.java
│   │       │   ├── JobRequisition.java
│   │       │   ├── Offer.java
│   │       │   └── Recruiter.java     (Candidate data)
│   │       ├── repository/
│   │       │   ├── InterviewRepository.java
│   │       │   ├── JobRequisitionRepository.java
│   │       │   ├── OfferRepository.java
│   │       │   └── RecruiterRepository.java
│   │       └── service/
│   │           ├── InterviewService.java
│   │           ├── JobRequisitionService.java
│   │           ├── OfferService.java
│   │           └── RecruiterService.java
│   └── src/main/resources/
│       ├── application.properties     (App configuration)
│       └── static/                    (Frontend HTML/JS/CSS served by backend)
├── frontend/                          (Standalone HTML pages)
│   ├── login.html
│   ├── employee.html
│   ├── dashboard.html
│   ├── payroll.html
│   ├── admin.html
│   ├── attendance.html
│   ├── appraisal.html
│   ├── profile.html
│   ├── payslip.html
│   └── scripts/
│       ├── login.js
│       ├── employee.js
│       └── payrollApi.js
```

---

## How to Run the Project

Prerequisites:
- Java 21 installed
- MySQL installed and running on port 3306
- Maven installed (or use the provided mvnw wrapper)

Steps:

1. Start MySQL and make sure it is running on localhost:3306 with username "root" and password "root".

2. Open a terminal in the backend folder:
   ```
   cd C:\Users\2505474\Desktop\Project-hrms\hrms-payroll-system\backend
   ```

3. Run the database schema script in MySQL:
   ```
   mysql -u root -p < database_schema.sql
   ```

4. Start the Spring Boot application:
   ```
   .\mvnw.cmd spring-boot:run
   ```
   Or use the provided batch file:
   ```
   start-application.bat
   ```

5. Open a browser and go to: http://localhost:8080

---

## Database Setup

The application connects to MySQL with these settings (from application.properties):

| Property    | Value                                            |
|-------------|--------------------------------------------------|
| URL         | jdbc:mysql://localhost:3306/hrms                  |
| Username    | root                                             |
| Password    | root                                             |
| DDL Mode    | update (auto-creates/updates tables on startup)  |

The database name is "hrms". It gets created automatically if it does not exist.

---

## Module 1 - Employee Management

### What it does

This module lets you manage employee records. You can:
- Add a new employee
- View all employees or one employee by ID
- Update employee details
- Delete an employee
- Assign a manager to an employee

### Model: Employee

The Employee class represents one row in the "employees" table.

| Field       | Type       | Description                                 |
|-------------|------------|---------------------------------------------|
| id          | Integer    | Auto-generated unique ID                    |
| name        | String     | Employee full name (required)               |
| role        | String     | Job role like "Developer" (required)        |
| department  | String     | Department like "Engineering" (required)    |
| salary      | Double     | Monthly salary (must be positive, required) |
| hireDate    | LocalDate  | Date the employee was hired (required)      |
| designation | String     | Specific title like "Software Engineer"     |
| status      | String     | Active, Inactive, or On Leave               |
| manager     | Employee   | Reference to another Employee (the manager) |

### Model: User

The User class represents login credentials stored in the "users" table.

| Field     | Type    | Description                          |
|-----------|---------|--------------------------------------|
| id        | Integer | Auto-generated unique ID             |
| loginname | String  | Username for login (unique)          |
| password  | String  | Password                             |
| role      | String  | Role like "EMPLOYEE" or "HR_RECRUITER" |

### How Login Works

1. User sends loginname and password to POST /api/login/login
2. The system checks if the credentials match a record in the users table
3. If valid and role is "EMPLOYEE", it returns the employee page URL
4. If valid and role is "HR_RECRUITER", it returns the recruiter page URL
5. If invalid, it returns 401 Unauthorized

### Service Layer: EmployeeService

This class contains the business logic. It calls the repository to read/write data.

Key methods:
- createEmployee(employee) - saves a new employee to the database
- getEmployeeById(id) - finds one employee or throws an error if not found
- getAllEmployees() - returns every employee
- updateEmployee(id, employee) - finds existing record and updates all fields
- deleteEmployee(id) - removes an employee
- assignManager(id, managerId) - sets one employee as the manager of another

### Repository: EmployeeRepository

This is an interface (not a class) that extends JpaRepository. Spring automatically provides the implementation. It gives you methods like save(), findById(), findAll(), delete() without writing any SQL.

---

## Module 2 - Recruitment Management

### What it does

This module manages the entire hiring process:
- Track candidates who applied
- Create job requisitions (open positions)
- Schedule and manage interviews
- Roll out and manage offer letters
- View a dashboard with counts of each entity

### Model: Recruiter (Candidate)

Represents a job candidate stored in the "Candidate" table.

| Field           | Type            | Description                                    |
|-----------------|-----------------|------------------------------------------------|
| candidateId     | Integer         | Auto-generated unique ID                       |
| fullName        | String          | Candidate name                                 |
| appliedRole     | String          | The job role they applied for                  |
| experienceYears | Integer         | Years of work experience (0-50)                |
| interviewStage  | String          | Current stage like "Technical Round"           |
| candidateStatus | CandidateStatus | APPLIED, IN_INTERVIEW, OFFERED, HIRED, REJECTED |

### Model: JobRequisition

Represents a job opening that the company wants to fill.

| Field             | Type     | Description                            |
|-------------------|----------|----------------------------------------|
| requisitionId     | Integer  | Auto-generated unique ID               |
| jobTitle          | String   | Title like "Full Stack Developer"      |
| department        | String   | Which department needs this hire       |
| numberOfPositions | Integer  | How many people to hire (1-100)        |
| priority          | Priority | LOW, MEDIUM, HIGH, URGENT             |
| status            | Status   | OPEN, IN_PROGRESS, FILLED, CLOSED     |
| description       | String   | Detailed job description (up to 2000 chars) |
| requisitionDate   | LocalDate| Date when this request was made        |

### Model: Interview

Represents a scheduled interview for a candidate.

| Field            | Type            | Description                                    |
|------------------|-----------------|------------------------------------------------|
| interviewId      | Integer         | Auto-generated unique ID                       |
| candidateId      | Integer         | Links to a candidate                           |
| interviewerName  | String          | Name of the person conducting interview        |
| interviewDateTime| LocalDateTime   | Date and time of the interview                 |
| interviewMode    | String          | Online, Offline, or Phone                      |
| location         | String          | Physical location (if offline)                 |
| meetingLink      | String          | Video call link (if online)                    |
| interviewRound   | String          | Technical, HR, or Managerial                   |
| interviewStatus  | InterviewStatus | SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED, NO_SHOW |
| remarks          | String          | Notes about the interview                      |

### Model: Offer

Represents an offer letter sent to a candidate.

| Field              | Type        | Description                                 |
|--------------------|-------------|---------------------------------------------|
| offerId            | Integer     | Auto-generated unique ID                    |
| candidateId        | Integer     | Links to a candidate                        |
| positionOffered    | String      | Job title being offered                     |
| department         | String      | Department for the position                 |
| salaryOffered      | BigDecimal  | Annual salary amount                        |
| offerDate          | LocalDate   | Date offer was made                         |
| joiningDate        | LocalDate   | Expected start date                         |
| offerStatus        | OfferStatus | DRAFTED, SENT, ACCEPTED, REJECTED, WITHDRAWN |
| additionalBenefits | String      | Perks like "Health Insurance, Stock Options"|
| remarks            | String      | Additional notes                            |

### Recruitment Business Logic

The services in this module have important business rules:

1. When an interview is scheduled, the candidate status automatically changes to IN_INTERVIEW.

2. When an interview is marked CANCELLED or NO_SHOW, the candidate status changes to REJECTED.

3. Before rolling out an offer, the system checks that the candidate has at least one COMPLETED interview. If not, the offer cannot be created.

4. When an offer is rolled out, the candidate status changes to OFFERED.

5. When an offer status is changed to ACCEPTED, the candidate status changes to HIRED.

### Controller: RecruitmentRestController

This single controller handles all recruitment-related HTTP requests. It is mapped to /api/recruitment and provides endpoints for candidates, job requisitions, interviews, and offers.

---

## Module 3 - Frontend Pages

The frontend is a collection of HTML pages with JavaScript that calls the backend REST APIs.

| Page              | Purpose                                  |
|-------------------|------------------------------------------|
| login.html        | Login form for employees and recruiters  |
| role-select.html  | Select role before login                 |
| employee.html     | Employee self-service dashboard          |
| dashboard.html    | General dashboard view                   |
| profile.html      | Employee profile details                 |
| attendance.html   | Attendance and leave management          |
| payroll.html      | Payroll management (HR view)             |
| payslip.html      | Employee payslip view                    |
| appraisal.html    | Performance appraisal                    |
| admin.html        | Admin panel                              |

---

## API Reference

### Employee APIs (Base: /api/employees)

| Method | Endpoint                      | Description                   |
|--------|-------------------------------|-------------------------------|
| GET    | /api/employees                | Get all employees             |
| GET    | /api/employees/{id}           | Get one employee by ID        |
| POST   | /api/employees                | Create a new employee         |
| PUT    | /api/employees/{id}           | Update an employee            |
| DELETE | /api/employees/{id}           | Delete an employee            |
| PUT    | /api/employees/{id}/manager/{managerId} | Assign a manager  |
| GET    | /api/employees/{id}/getName   | Get employee name only        |
| GET    | /api/employees/{id}/getRole   | Get employee role only        |
| GET    | /api/employees/{id}/getDepartment | Get department only       |
| GET    | /api/employees/{id}/getSalary | Get salary only               |
| GET    | /api/employees/{id}/getHireDate | Get hire date only          |
| GET    | /api/employees/{id}/getDesignation | Get designation only     |
| GET    | /api/employees/{id}/getStatus | Get status only               |
| GET    | /api/employees/{id}/getManager | Get manager details          |

### Login API

| Method | Endpoint           | Description                              |
|--------|--------------------|------------------------------------------|
| POST   | /api/login/login   | Authenticate user (params: loginname, password) |

### Recruitment APIs (Base: /api/recruitment)

| Method | Endpoint                                 | Description                        |
|--------|------------------------------------------|------------------------------------|
| GET    | /api/recruitment/health                  | Check if API is running            |
| GET    | /api/recruitment/dashboard               | Get counts of all entities         |
| POST   | /api/recruitment/candidates              | Add a new candidate                |
| GET    | /api/recruitment/candidates              | Get all candidates                 |
| GET    | /api/recruitment/candidates/{id}         | Get one candidate                  |
| PUT    | /api/recruitment/candidates/{id}         | Update a candidate                 |
| DELETE | /api/recruitment/candidates/{id}         | Delete a candidate                 |
| POST   | /api/recruitment/job-requisitions        | Create a job requisition           |
| GET    | /api/recruitment/job-requisitions        | Get all job requisitions           |
| GET    | /api/recruitment/job-requisitions/{id}   | Get one job requisition            |
| PUT    | /api/recruitment/job-requisitions/{id}   | Update a job requisition           |
| DELETE | /api/recruitment/job-requisitions/{id}   | Delete a job requisition           |
| POST   | /api/recruitment/interviews              | Schedule an interview              |
| GET    | /api/recruitment/interviews              | Get all interviews                 |
| GET    | /api/recruitment/interviews/{id}         | Get one interview                  |
| GET    | /api/recruitment/interviews/candidate/{candidateId} | Get interviews for a candidate |
| PUT    | /api/recruitment/interviews/{id}         | Update an interview                |
| DELETE | /api/recruitment/interviews/{id}         | Delete an interview                |
| POST   | /api/recruitment/offers                  | Roll out an offer                  |
| GET    | /api/recruitment/offers                  | Get all offers                     |
| GET    | /api/recruitment/offers/{id}             | Get one offer                      |
| GET    | /api/recruitment/offers/candidate/{candidateId} | Get offers for a candidate |
| PUT    | /api/recruitment/offers/{id}             | Update an offer                    |
| PATCH  | /api/recruitment/offers/{id}/status      | Update offer status only           |
| DELETE | /api/recruitment/offers/{id}             | Delete an offer                    |

---

## How the Code Layers Work

This project follows a layered architecture. Each layer has a specific job. Here is what each layer does, explained simply:

### 1. Model (also called Entity)

- These are plain Java classes that represent a database table.
- Each field in the class becomes a column in the table.
- Annotations like @Entity, @Table, @Id tell the framework how to map the class to the database.
- Example: Employee.java has fields like name, salary, department. These become columns in the "employees" table.

### 2. Repository

- This is an interface (not a class) that extends JpaRepository.
- You do NOT write the code for basic operations. Spring generates it for you.
- It automatically gives you: save(), findById(), findAll(), deleteById(), count()
- You can add custom methods just by writing a method name like findByCandidateId(Integer id) and Spring figures out the SQL.

### 3. Service

- This is where the business logic lives.
- It sits between the Controller and Repository.
- Example: Before creating an offer, OfferService checks if the candidate has a completed interview. This is a business rule that does not belong in the controller or repository.
- Services call repository methods to get or save data.

### 4. Controller

- This is the entry point for HTTP requests.
- It receives requests from the frontend (browser), calls the service, and returns a response.
- Annotations like @GetMapping, @PostMapping, @PutMapping, @DeleteMapping define which URL and HTTP method trigger which function.
- Example: A GET request to /api/employees calls EmployeeController.getAllEmployees(), which calls EmployeeService.getAllEmployees(), which calls EmployeeRepository.findAll().

### Request Flow (Step by Step)

```
Browser (Frontend)
    |
    | HTTP Request (e.g., GET /api/employees)
    v
Controller (EmployeeController)
    |
    | Calls service method
    v
Service (EmployeeService)
    |
    | Calls repository method
    v
Repository (EmployeeRepository)
    |
    | Executes SQL query
    v
MySQL Database
    |
    | Returns data
    v
(Data flows back up through each layer to the browser)
```

### 5. Configuration

- WebConfig.java sets up CORS (allows frontend on a different port to call the backend API) and tells Spring where to find static HTML/CSS/JS files.
- application.properties contains database connection details, logging settings, and other configuration.

---

## Database Tables

| Table Name     | Module      | Purpose                                |
|----------------|-------------|----------------------------------------|
| employees      | Employee    | Stores employee records                |
| users          | Employee    | Stores login credentials               |
| Candidate      | Recruitment | Stores job candidate information       |
| JobRequisition | Recruitment | Stores open job positions              |
| Interview      | Recruitment | Stores interview schedule and results  |
| OfferLetter    | Recruitment | Stores offer letters sent to candidates|
| Employee       | Future      | Extended employee table (schema only)  |
| LeaveRequest   | Future      | Leave management (schema only)         |
| PayrollRecord  | Future      | Payroll records (schema only)          |
| AppraisalRecord| Future      | Performance reviews (schema only)      |

---

## Key Concepts for Beginners

### What is Spring Boot?

Spring Boot is a Java framework that makes it easy to create web applications. It handles a lot of setup automatically so you can focus on writing your business logic. You do not need to configure a server manually. Just run the application and it starts a built-in web server on port 8080.

### What is JPA?

JPA (Java Persistence API) is a way to save Java objects directly to a database without writing SQL queries manually. You write a Java class, mark it with annotations, and the framework creates the database table and handles all the read/write operations.

### What is REST API?

REST API is a way for the frontend (browser) to communicate with the backend (server). The frontend sends HTTP requests (GET, POST, PUT, DELETE) to specific URLs, and the backend responds with data (usually in JSON format).

- GET = retrieve data (like reading from a database)
- POST = create new data (like inserting a new row)
- PUT = update existing data (like modifying a row)
- DELETE = remove data (like deleting a row)

### What is Lombok?

Lombok is a library that automatically generates repetitive code like getters, setters, constructors, and toString methods. Instead of writing 50 lines of boilerplate, you add one annotation like @Data.

- @Data = generates getters, setters, toString, equals, hashCode
- @NoArgsConstructor = generates a constructor with no arguments
- @AllArgsConstructor = generates a constructor with all fields as arguments

### What is Maven?

Maven is a build tool. The pom.xml file lists all the libraries (dependencies) your project needs. Maven downloads them automatically and compiles your project. You do not manually download JAR files.

### What is CORS?

CORS (Cross-Origin Resource Sharing) is a security feature in browsers. If your frontend runs on localhost:5500 and your backend runs on localhost:8080, the browser blocks requests by default. The WebConfig.java file configures the backend to allow requests from any origin.

### What are Annotations?

Annotations are special markers that start with @ symbol. They tell the framework how to treat your code:

- @Entity = this class maps to a database table
- @Id = this field is the primary key
- @RestController = this class handles HTTP requests
- @Service = this class contains business logic
- @Repository = this class talks to the database
- @Autowired = inject a dependency automatically
- @GetMapping("/path") = handle GET requests to this path
- @PostMapping("/path") = handle POST requests to this path
- @NotBlank = this field cannot be empty (validation)
- @NotNull = this field cannot be null (validation)

---

## Summary

This HRMS project has two main working modules:

1. Employee Module - CRUD operations for employees plus login authentication
2. Recruitment Module - Full hiring workflow from candidate application through interview to offer letter

The frontend communicates with the backend through REST APIs. The backend uses Spring Boot with JPA to handle data storage in MySQL. The code follows a clean Controller-Service-Repository pattern where each layer has a clear responsibility.

The project is designed so that each module works independently. The employee module handles employee data and login. The recruitment module handles the full hiring pipeline. Both share the same database and run as one Spring Boot application.

