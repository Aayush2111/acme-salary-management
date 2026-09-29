# ACME Salary Management – API Reference

Base path: `/api`. All request and response bodies are JSON.

## Employee Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/employees` | List employees (paginated, searchable, filterable, sortable) |
| GET | `/employees/{id}` | Get one employee |
| POST | `/employees` | Create an employee |
| PUT | `/employees/{id}` | Update an employee |
| DELETE | `/employees/{id}` | Delete an employee |

### Listing, search, filter, sort

`GET /api/employees` query parameters:

| Param | Description |
|---|---|
| `page`, `size` | Pagination (`page` is 0-indexed) |
| `sort` | e.g. `salary,desc` |
| `search` | Matches name, employee number, or email |
| `department`, `country` | Exact-match filters |

Example:

```http
GET /api/employees?search=john&department=Engineering&page=0&size=20&sort=salary,desc
```

### Employee record shape

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

Used as the request body for create/update (minus `id`), and returned by
every endpoint above. `POST`/`PUT` return `201`/`200` with the resulting
record; `DELETE` returns `204`. An unknown `id` returns `404`.

### Validation rules

| Field | Rule |
|---|---|
| employeeNumber, firstName, lastName, department, country, jobTitle | Required |
| email | Required, valid email format |
| salary | Required, greater than zero |
| currency | Exactly 3 characters |

## Salary Analytics Endpoints

| Method | Endpoint | Returns |
|---|---|---|
| GET | `/salary/summary` | `{ totalEmployees, averageSalary, minimumSalary, maximumSalary }` |
| GET | `/salary/by-department` | `[{ department, employeeCount, averageSalary }]`, sorted by average salary desc |
| GET | `/salary/by-country` | `[{ country, employeeCount, averageSalary }]`, sorted by average salary desc |
| GET | `/salary/distribution` | `[{ salaryRange, employeeCount }]` across `<500K`, `500K-999K`, `1M-1.49M`, `1.5M-1.99M`, `2M+` |

## Error Responses

Centralized via `@RestControllerAdvice`. All errors share this shape:

```json
{
  "status": 404,
  "message": "Employee not found with id: 999",
  "timestamp": "2026-09-28T10:00:00Z"
}
```

| Status | When |
|---|---|
| 400 | Validation failure |
| 404 | Employee not found |
| 409 | Duplicate employee number or email |

## Design Principles

Resource-oriented REST, JSON in/out, DTOs only (JPA entities are never
exposed directly), database-level pagination, centralized error handling.
