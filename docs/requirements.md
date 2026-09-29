# ACME Salary Management – Requirements

## Goal

Give the HR Manager a web app to manage employee salary records and
understand salary distribution across the organization, replacing
Excel-based tracking. The app must stay responsive and maintainable with
~10,000 employees.

## Scope

**Employee Management** — HR can view employees (paginated), search by
name/employee number/email, filter by department and country, sort, and
add, update, or delete an employee.

**Salary Analytics** — a dashboard shows total employees, average/min/max
salary, average salary by department, average salary by country, and
salary distribution across predefined ranges.

**Employee Record Fields** — employee number, first name, last name,
email, department, country, job title, salary, currency.

Records are validated (required fields, valid email, positive salary), and
the app ships with deterministic seed data for 10,000 employees to support
development, testing, and demos.

## Non-Functional Requirements

- REST APIs follow resource-oriented conventions, backed by a Controller → Service → Repository structure.
- Business logic has meaningful automated test coverage.
- Errors return appropriate HTTP status codes with useful messages.
- Employee listing is paginated at the database level, not loaded in full into memory.
- The UI is simple and responsive for HR users.

## Deliberately Out of Scope

- **Authentication/authorization** — confirmed with HR that this is not
  required; a single HR Manager user/role is assumed. This is why no login
  flow (e.g. JWT-based auth) was implemented — there's no second role or
  external user to protect the API from in this assessment's scope.
- **Payroll processing** — no tax, deductions, benefits, or payslips; this is salary management, not payroll.
- **Employee self-service** — built for the HR Manager persona only.
- **Historical salary tracking** — no audit trail, current data only.
- **Currency handling** — intentionally left open by the brief (store local
  currencies, normalize to one reporting currency, or both, with a
  fixed/deterministic FX rate if converting). This app stores each
  employee's salary together with their local currency and does not
  perform conversion or normalization, which is one of the acceptable
  approaches under that flexibility.

## Success Criteria

An HR Manager can manage employee salary records, search/filter the
10,000-employee dataset, and understand salary distribution through the
dashboard — without relying on Excel.
