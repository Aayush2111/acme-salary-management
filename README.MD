# ACME Salary Management

A web app for HR to manage employee salary records and view salary analytics,
replacing Excel-based tracking. Built with Spring Boot, Angular + Angular
Material, and MySQL.

## Features

- Employee CRUD with validation
- Search, filter (department/country), sort, and pagination across ~10,000 employees
- Salary dashboard: summary stats, by-department, by-country, and distribution
- Deterministic seed data (10,000 employees) generated automatically on first startup

## Project Structure

- `backend/` — Spring Boot REST API
- `frontend/` — Angular + Angular Material UI
- `docs/` — requirements, architecture, API reference, AI usage notes

## Running with Docker (recommended)

```bash
docker compose up --build
```

- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- MySQL: localhost:3307 (database `acme_salary`, user/password `root`/`root`)

## Running locally without Docker

1. Start a local MySQL instance (see `backend/salary-management/src/main/resources/application.properties` for default connection settings).
2. Backend: `cd backend/salary-management && ./mvnw spring-boot:run`
3. Frontend: `cd frontend && ng serve`, then open http://localhost:4200

## Tests

- Backend: `./mvnw test` (from `backend/salary-management`)
- Frontend: `ng test` (from `frontend`)

## Docs

- [Requirements](docs/requirements.md)
- [Architecture](docs/architecture.md)
- [API Reference](docs/api.md)
- [AI Usage & Trade-offs](docs/ai-usage.md)
