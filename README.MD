# ACME Salary Management

Employee salary management application built with Spring Boot, Angular and MySQL.

## Project Structure

- `backend/` - Spring Boot backend
- `frontend/` - Angular frontend
- `docs/` - Project documentation

## Running with Docker

The full stack (MySQL, backend, frontend) can be run locally with Docker Compose:

```bash
docker compose up --build
```

- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- MySQL: localhost:3307 (database `acme_salary`, user/password `root`/`root`)

The backend seeds 10,000 employees on first startup once MySQL is reachable.
To run without Docker, start a local MySQL instance and run the backend
(`./mvnw spring-boot:run`) and frontend (`ng serve`) separately — see
`backend/salary-management/src/main/resources/application.properties` for the
default connection settings.