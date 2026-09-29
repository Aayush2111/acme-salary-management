# ACME Salary Management – Architecture

## Overview

A modular monolith: one deployable Spring Boot backend, layered by
responsibility, backing an Angular frontend.

```text
Angular Frontend
      |  HTTP / REST
      v
REST Controllers
      |
      v
   Services   (business logic)
      |
      v
 Repositories (JPA)
      |
      v
     MySQL
```

A single deployable keeps things simple for this scope while still
separating concerns cleanly. Individual modules could be extracted into
services later if the domain grows.

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular, Angular Material, TypeScript |
| Backend | Spring Boot, Spring Web, Spring Data JPA, Hibernate, Bean Validation |
| Database | MySQL |
| Testing | JUnit, Mockito, Spring MockMvc |

## Backend Package Structure

```text
salary_management
├── config          DataSeeder, CorsConfig
├── controller      EmployeeController, SalaryController
├── dto             EmployeeRequest/Response, Salary*Response
├── entity          Employee
├── exception       EmployeeNotFoundException, GlobalExceptionHandler
├── repository      EmployeeRepository
├── service         EmployeeService, EmployeeServiceImpl
└── specification   EmployeeSpecification
```

## Layer Responsibilities

- **Controller** — exposes REST endpoints, reads request params/bodies, delegates to services. No business logic.
- **Service** — CRUD logic, search/filter rules, salary analytics, entity ↔ DTO mapping, not-found handling.
- **Repository** — Spring Data JPA access, salary aggregation queries, dynamic filtering via JPA Specifications.
- **Entity/DTO** — `Employee` is the persistence model; separate request/response DTOs keep the API contract independent of the database schema.

## Search, Filter, Sort, Pagination

`GET /api/employees` supports all of these together:

```http
GET /api/employees?search=john&department=Engineering&country=India&page=0&size=20&sort=salary,desc
```

Filtering is built dynamically with JPA Specifications, and pagination/sorting
happen at the database level — important since the dataset is ~10,000
employees and shouldn't be loaded into memory at once.

## Salary Analytics

Four read-only endpoints back the dashboard:

| Endpoint | Returns |
|---|---|
| `/api/salary/summary` | total employees, avg/min/max salary |
| `/api/salary/by-department` | employee count + avg salary per department |
| `/api/salary/by-country` | employee count + avg salary per country |
| `/api/salary/distribution` | employee count per salary range (`<500K`, `500K-999K`, `1M-1.49M`, `1.5M-1.99M`, `2M+`) |

## Data Seeding

On startup, `DataSeeder` checks whether any employees exist; if not, it
generates 10,000 deterministic records (varied department/country/job
title/salary) and saves them in batches of 500. This keeps local dev and
demos reproducible without a manual data-loading step.

## Validation & Error Handling

Requests are validated with Jakarta Bean Validation (required fields, valid
email, positive salary, 3-letter currency code). A `@RestControllerAdvice`
centralizes error handling, returning a consistent
`{ status, message, timestamp }` body for validation failures (400),
not-found errors (404), and duplicate employee number/email (409).

## Testing Strategy

- **Service tests** (JUnit + Mockito) — CRUD behavior, not-found handling, all four analytics queries.
- **Controller tests** (MockMvc) — success paths, 404/400 handling, response shape.

Tests focus on real behavior, not trivial getters/setters.

## Trade-offs

**Modular monolith over microservices** — the domain (employee salary
management) is focused enough that splitting into services would add
deployment/communication complexity without a matching benefit. Modules
could be extracted later if the domain grows.

**MySQL over NoSQL** — the data is inherently relational (structured
employee records, aggregation queries for analytics), so a relational
database was the natural fit.

**REST over GraphQL/gRPC** — the frontend's needs are straightforward CRUD
plus a handful of analytics reads; REST covers this without extra tooling.

## Scalability Considerations

The current design assumes ~10,000 employees and handles that scale through
database-level pagination/filtering/aggregation, batched seeding, and unique
constraints on employee number/email. If the dataset grows substantially,
the next steps would be additional indexes, moving distribution calculation
into a DB aggregation, caching analytics, and read replicas — intentionally
not built now since they aren't needed at this scale.
