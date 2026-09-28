# ACME Salary Management – API Documentation

## 1. Overview

The ACME Salary Management backend exposes REST APIs for:

- Employee management
- Employee search and filtering
- Pagination and sorting
- Salary summary analytics
- Salary analytics by department
- Salary analytics by country
- Salary distribution

The API uses JSON for request and response bodies.

Base path:

```text
/api
```

---

# 2. Employee APIs

## 2.1 Get Employees

Returns a paginated list of employees.

### Endpoint

```http
GET /api/employees
```

### Query Parameters

| Parameter | Required | Description |
|---|---|---|
| `page` | No | Page number, starting from 0 |
| `size` | No | Number of employees per page |
| `sort` | No | Field and direction, for example `salary,desc` |
| `search` | No | Searches by employee name, employee number, or email |
| `department` | No | Filters employees by department |
| `country` | No | Filters employees by country |

### Example

```http
GET /api/employees?page=0&size=20&sort=salary,desc
```

### Search Example

```http
GET /api/employees?search=john&page=0&size=20
```

### Filter Example

```http
GET /api/employees?department=Engineering&country=India&page=0&size=20
```

### Response

The endpoint returns a paginated response containing employee records and
pagination metadata.

Example employee record:

```json
{
  "id": 1,
  "employeeNumber": "EMP00001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@acme.com",
  "department": "Engineering",
  "country": "India",
  "jobTitle": "Software Engineer",
  "salary": 800000,
  "currency": "INR"
}
```

---

## 2.2 Get Employee by ID

Returns a single employee by ID.

### Endpoint

```http
GET /api/employees/{id}
```

### Example

```http
GET /api/employees/1
```

### Success Response

```text
200 OK
```

### Employee Not Found

```text
404 Not Found
```

---

## 2.3 Create Employee

Creates a new employee.

### Endpoint

```http
POST /api/employees
```

### Request Body

```json
{
  "employeeNumber": "EMP10001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@acme.com",
  "department": "Engineering",
  "country": "India",
  "jobTitle": "Software Engineer",
  "salary": 800000,
  "currency": "INR"
}
```

### Success Response

```text
201 Created
```

The response contains the created employee.

---

## 2.4 Update Employee

Updates an existing employee.

### Endpoint

```http
PUT /api/employees/{id}
```

### Example

```http
PUT /api/employees/1
```

### Request Body

```json
{
  "employeeNumber": "EMP00001",
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@acme.com",
  "department": "Engineering",
  "country": "India",
  "jobTitle": "Senior Software Engineer",
  "salary": 1000000,
  "currency": "INR"
}
```

### Success Response

```text
200 OK
```

### Employee Not Found

```text
404 Not Found
```

---

## 2.5 Delete Employee

Deletes an employee.

### Endpoint

```http
DELETE /api/employees/{id}
```

### Example

```http
DELETE /api/employees/1
```

### Success Response

```text
204 No Content
```

### Employee Not Found

```text
404 Not Found
```

---

# 3. Salary Analytics APIs

## 3.1 Salary Summary

Returns overall salary statistics for the organization.

### Endpoint

```http
GET /api/salary/summary
```

### Response

```json
{
  "totalEmployees": 10000,
  "averageSalary": 1750000,
  "minimumSalary": 500000,
  "maximumSalary": 2999000
}
```

The response contains:

- Total number of employees
- Average salary
- Minimum salary
- Maximum salary

---

## 3.2 Salary by Department

Returns employee count and average salary grouped by department.

### Endpoint

```http
GET /api/salary/by-department
```

### Example Response

```json
[
  {
    "department": "Engineering",
    "employeeCount": 1250,
    "averageSalary": 1850000
  },
  {
    "department": "Finance",
    "employeeCount": 1100,
    "averageSalary": 1650000
  }
]
```

Results are ordered by average salary in descending order.

---

## 3.3 Salary by Country

Returns employee count and average salary grouped by country.

### Endpoint

```http
GET /api/salary/by-country
```

### Example Response

```json
[
  {
    "country": "India",
    "employeeCount": 3000,
    "averageSalary": 1600000
  },
  {
    "country": "United States",
    "employeeCount": 1800,
    "averageSalary": 2200000
  }
]
```

Results are ordered by average salary in descending order.

---

## 3.4 Salary Distribution

Returns the number of employees within each predefined salary range.

### Endpoint

```http
GET /api/salary/distribution
```

### Salary Ranges

```text
< 500K
500K - 999K
1M - 1.49M
1.5M - 1.99M
2M+
```

### Example Response

```json
[
  {
    "salaryRange": "< 500K",
    "employeeCount": 500
  },
  {
    "salaryRange": "500K - 999K",
    "employeeCount": 1800
  },
  {
    "salaryRange": "1M - 1.49M",
    "employeeCount": 2500
  },
  {
    "salaryRange": "1.5M - 1.99M",
    "employeeCount": 2700
  },
  {
    "salaryRange": "2M+",
    "employeeCount": 2500
  }
]
```

---

# 4. Employee Request Validation

Employee creation and update requests are validated before processing.

The following validations are currently applied:

| Field | Validation |
|---|---|
| Employee number | Required |
| First name | Required |
| Last name | Required |
| Email | Required and valid email format |
| Department | Required |
| Country | Required |
| Job title | Required |
| Salary | Required and greater than zero |
| Currency | Exactly 3 characters |

---

# 5. Error Handling

The application uses centralized exception handling through
`@RestControllerAdvice`.

## 5.1 Validation Error

### Status

```text
400 Bad Request
```

Example:

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2026-09-28T10:00:00Z"
}
```

---

## 5.2 Employee Not Found

### Status

```text
404 Not Found
```

Example:

```json
{
  "status": 404,
  "message": "Employee not found with id: 999",
  "timestamp": "2026-09-28T10:00:00Z"
}
```

---

# 6. API Design Principles

The API follows the following principles:

- Resource-oriented REST endpoints.
- HTTP methods represent operations.
- JSON request and response bodies.
- Appropriate HTTP status codes.
- Pagination for employee listing.
- Database-backed filtering and sorting.
- Centralized error handling.
- DTOs are used as the API contract.
- JPA entities are not directly exposed as API responses.

---

# 7. API Summary

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/employees` | List, search, filter and paginate employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |
| GET | `/api/salary/summary` | Overall salary statistics |
| GET | `/api/salary/by-department` | Salary statistics by department |
| GET | `/api/salary/by-country` | Salary statistics by country |
| GET | `/api/salary/distribution` | Salary distribution |
