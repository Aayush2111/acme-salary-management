# ACME Salary Management – Architecture

## 1. Architecture Overview

The application follows a **modular monolith architecture**.

The backend is organized into clear layers, with each layer having a
specific responsibility.

```text
                    +----------------------+
                    |   Angular Frontend   |
                    +----------+-----------+
                               |
                               | HTTP / REST
                               v
                    +----------------------+
                    |   REST Controllers   |
                    +----------+-----------+
                               |
                               v
                    +----------------------+
                    |      Services        |
                    |    Business Logic    |
                    +----------+-----------+
                               |
                               v
                    +----------------------+
                    |     Repositories     |
                    |   Data Access / JPA  |
                    +----------+-----------+
                               |
                               v
                    +----------------------+
                    |        MySQL         |
                    +----------------------+
```

The application is intentionally implemented as a single deployable backend
rather than being split into multiple microservices.

This keeps the solution simple and maintainable for the current scope while
still providing clear separation between responsibilities.

---

## 2. Technology Stack

### Frontend

- Angular
- TypeScript
- Angular Material

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Bean Validation
- Maven

### Database

- MySQL

### Testing

- JUnit
- Mockito
- Spring MockMvc

---

## 3. Backend Package Structure

```text
salary_management
├── config
│   └── DataSeeder
│
├── controller
│   ├── EmployeeController
│   └── SalaryController
│
├── dto
│   ├── EmployeeRequest
│   ├── EmployeeResponse
│   ├── SalarySummaryResponse
│   ├── DepartmentSalaryResponse
│   ├── CountrySalaryResponse
│   └── SalaryDistributionResponse
│
├── entity
│   └── Employee
│
├── exception
│   ├── EmployeeNotFoundException
│   ├── ErrorResponse
│   └── GlobalExceptionHandler
│
├── repository
│   └── EmployeeRepository
│
├── service
│   ├── EmployeeService
│   └── EmployeeServiceImpl
│
└── specification
    └── EmployeeSpecification
```

---

## 4. Layer Responsibilities

### Controller Layer

The controller layer is responsible for handling HTTP requests and responses.

Responsibilities include:

- Exposing REST endpoints.
- Reading request parameters and request bodies.
- Triggering request validation.
- Calling the appropriate service method.
- Returning appropriate HTTP responses.

Controllers do not contain business logic.

---

### Service Layer

The service layer contains the application's business logic.

Responsibilities include:

- Employee CRUD operations.
- Salary analytics.
- Applying search and filtering rules.
- Mapping entities to DTOs.
- Handling employee-not-found scenarios.

The service layer provides a boundary between the HTTP layer and the
persistence layer.

---

### Repository Layer

The repository layer handles database access through Spring Data JPA.

Responsibilities include:

- Employee CRUD operations.
- Salary aggregation queries.
- Retrieving salary values for distribution analysis.
- Supporting dynamic employee filtering through JPA Specifications.

The service layer interacts with the repository instead of directly
accessing the database.

---

### Entity Layer

The `Employee` entity represents the employee record stored in MySQL.

It contains fields such as:

- Employee number
- First name
- Last name
- Email
- Department
- Country
- Job title
- Salary
- Currency

The entity is used by the persistence layer and is not exposed directly as
the public REST API response.

---

### DTO Layer

DTOs define the API contract.

Separate request and response DTOs are used to avoid exposing the persistence
entity directly through the API.

For example:

```text
EmployeeRequest
       |
       v
Controller
       |
       v
Service
       |
       v
Employee Entity
```

And for responses:

```text
Employee Entity
       |
       v
Service
       |
       v
EmployeeResponse
       |
       v
Controller
       |
       v
HTTP Response
```

This separation makes the API easier to evolve independently of the
database model.

---

## 5. Employee Management Flow

A typical employee retrieval request follows this flow:

```text
Client
  |
  | GET /api/employees/1
  v
EmployeeController
  |
  v
EmployeeService
  |
  v
EmployeeRepository
  |
  v
MySQL
  |
  v
EmployeeRepository
  |
  v
EmployeeService
  |
  | EmployeeResponse
  v
EmployeeController
  |
  v
Client
```

For create and update operations, the request DTO is validated before the
service performs the operation.

---

## 6. Search, Filtering, Pagination and Sorting

The employee listing API supports:

- Search by first name.
- Search by last name.
- Search by employee number.
- Search by email.
- Filtering by department.
- Filtering by country.
- Pagination.
- Sorting.

Dynamic filtering is implemented using Spring Data JPA Specifications.

The search conditions are constructed dynamically depending on which
parameters are provided.

For example:

```text
GET /api/employees?search=john
```

can search across multiple employee fields.

Filters can also be combined:

```text
GET /api/employees?department=Engineering&country=India
```

Pagination is performed at the database/query level rather than loading
the complete employee dataset into application memory.

This is important because the assessment targets approximately 10,000
employees.

---

## 7. Salary Analytics

Salary analytics are exposed through dedicated REST endpoints.

```text
/api/salary/summary
/api/salary/by-department
/api/salary/by-country
/api/salary/distribution
```

### Salary Summary

Provides:

- Total employees.
- Average salary.
- Minimum salary.
- Maximum salary.

### Salary by Department

Groups employees by department and provides:

- Department.
- Employee count.
- Average salary.

### Salary by Country

Groups employees by country and provides:

- Country.
- Employee count.
- Average salary.

### Salary Distribution

Employees are grouped into predefined salary ranges.

The current ranges are:

```text
< 500K
500K - 999K
1M - 1.49M
1.5M - 1.99M
2M+
```

Database aggregation is used where appropriate, while the salary
distribution bands are calculated by the application from the retrieved
salary values.

---

## 8. Data Seeding

The application provides deterministic seed data for 10,000 employees.

The seed process:

1. Checks whether employee data already exists.
2. Generates deterministic employee records when the database is empty.
3. Saves records in batches.
4. Skips seeding when employee data already exists.

The seed data includes different:

- Departments.
- Countries.
- Job titles.
- Salary values.

Deterministic data makes local development and demonstrations reproducible.

---

## 9. Validation and Error Handling

Request validation is implemented using Jakarta Bean Validation.

Examples of validation include:

- Required employee fields.
- Valid email format.
- Positive salary.
- Three-character currency code.

Application errors are handled centrally using `@RestControllerAdvice`.

Examples:

```text
Invalid request
      |
      v
400 Bad Request
```

```text
Employee not found
      |
      v
404 Not Found
```

The API returns a consistent error response containing:

- HTTP status.
- Error message.
- Timestamp.

This keeps error handling consistent across REST endpoints.

---

## 10. Testing Strategy

Testing focuses on meaningful application behavior rather than testing
trivial getters and setters.

### Service Tests

Service tests use JUnit and Mockito to verify:

- Employee retrieval.
- Employee creation.
- Employee update.
- Employee deletion.
- Employee-not-found behavior.
- Salary summary.
- Department salary analytics.
- Country salary analytics.
- Salary distribution.

### Controller Tests

Controller tests use Spring MockMvc to verify:

- Successful employee retrieval.
- HTTP 404 when an employee does not exist.
- HTTP 400 for invalid employee requests.
- JSON response structure.

The tests are designed to be fast, deterministic, and easy to understand.

---

## 11. Architectural Trade-offs

### Why a Modular Monolith?

The current application has a focused business domain:

> Employee salary management and salary analysis.

Splitting the application into microservices would introduce additional
complexity such as:

- Multiple deployments.
- Service-to-service communication.
- Distributed configuration.
- Additional monitoring requirements.
- More complex local development.

A modular monolith provides clear separation of responsibilities while
remaining simple to develop, test, deploy, and maintain.

If the domain grows significantly in the future, individual modules could
be extracted into independent services.

---

### Why MySQL?

MySQL is suitable for the structured employee and salary data used by the
application.

The relational model also supports:

- Employee CRUD operations.
- Filtering.
- Sorting.
- Pagination.
- Salary aggregation queries.

---

### Why REST?

REST provides a simple interface between the Angular frontend and Java
backend.

The application's requirements are primarily:

- CRUD operations.
- Search and filtering.
- Salary analytics.

REST is sufficient for these use cases without introducing additional
communication complexity.

---

## 12. Scalability Considerations

The initial target is approximately 10,000 employees.

The current design supports this through:

- Database-level pagination.
- Database-level filtering.
- Database-level aggregation where appropriate.
- Unique employee identifiers and email.
- Batched seed data insertion.
- Stateless REST APIs.

If the dataset grows substantially, additional optimization can be
considered, such as:

- Additional database indexes.
- Query optimization.
- Moving salary distribution calculation into database aggregation.
- Caching frequently accessed analytics.
- Read-optimized database strategies.

These optimizations are intentionally not introduced prematurely because
the current assessment scope is approximately 10,000 employees.

---

## 13. Deployment Architecture

The application is designed to have three main runtime components:

```text
+-------------------+
|  Angular Frontend |
+---------+---------+
          |
          | HTTP
          v
+-------------------+
|  Spring Boot API  |
+---------+---------+
          |
          | JDBC / JPA
          v
+-------------------+
|       MySQL       |
+-------------------+
```

The application can be run locally during development and deployed to a
publicly accessible environment for the final assessment.

No cloud-provider-specific architecture is required, allowing the
deployment environment to remain independent of the application design.

---

## 14. Design Principles

The implementation follows these principles:

### Separation of Concerns

Each layer has a clearly defined responsibility.

### Keep It Simple

Technology is introduced only when it solves a requirement or provides
clear value.

### Maintainability

The code is organized into focused classes and interfaces so that
individual components can be changed without affecting unrelated areas.

### Testability

Business logic is separated from controllers and persistence so it can be
tested independently.

### Database Efficiency

Filtering, pagination, and aggregation are performed close to the data
where practical rather than transferring unnecessary records to the
application.
