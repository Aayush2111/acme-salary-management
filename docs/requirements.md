# ACME Salary Management – Requirements

## 1. Goal

Build a web-based salary management system for the HR Manager to replace
Excel-based salary management.

The system should allow HR to manage employee salary information and
quickly understand how salaries are distributed across the organization.

The system should remain responsive and maintainable with approximately
10,000 employees.

## 2. Scope

### Employee Management

The HR Manager can:

- View employees in a paginated list.
- Search employees by name, employee number, or email.
- Filter employees by department and country.
- Sort employee records.
- Add a new employee.
- Update an employee.
- Delete an employee.
- View an employee's salary and employment information.

### Salary Analytics

The dashboard should provide:

- Total number of employees.
- Average salary.
- Minimum salary.
- Maximum salary.
- Average salary by department.
- Average salary by country.
- Salary distribution across predefined salary ranges.

### Data Management

Each employee record contains:

- Employee number
- First name
- Last name
- Email
- Department
- Country
- Job title
- Salary
- Currency

The application should provide validation for required fields and valid
salary/email values.

The application should include deterministic seed data for 10,000 employees
to support development, testing, and demonstration.

## 3. Non-Functional Requirements

- REST APIs should follow clear resource-oriented conventions.
- Backend code should follow a Controller → Service → Repository structure.
- Business logic should be covered by meaningful automated tests.
- API errors should return appropriate HTTP status codes and useful error
  responses.
- Employee listing should use pagination rather than loading the complete
  employee dataset.
- The application should be deployable and publicly accessible.
- The UI should provide a simple and responsive experience for HR users.
- The solution should be maintainable and easy to understand.

## 4. Deliberate Exclusions

### Authentication and Authorization

Authentication and role-based authorization are excluded from the initial
scope because the assessment assumes a single HR Manager user/role.

### Payroll Processing

Payroll calculation, tax calculation, deductions, benefits, bonuses, and
payslip generation are excluded because the goal is salary management and
salary analysis rather than payroll processing.

### Employee Self-Service

Employees cannot manage their own salary information. The system is
designed for the HR Manager persona.

### Historical Salary Tracking

Salary history and audit trails are excluded from the initial version to
keep the solution focused on the current salary dataset.

### Complex Currency Conversion

The initial version stores the employee's salary together with its currency.
Complex real-time foreign exchange integration is excluded because it is
not required to demonstrate salary management and analytics.

## 5. Success Criteria

The solution is successful when an HR Manager can manage employee salary
records, search/filter a 10,000-employee dataset, and understand salary
distribution through the dashboard without relying on Excel.