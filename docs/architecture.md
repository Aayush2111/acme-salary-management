# ACME Salary Management – Architecture

## 1. Architecture Overview

The application uses a modular monolith architecture.

The backend is organized into clear layers:

```text
Angular Frontend
       |
       | HTTP / REST
       v
+----------------------+
|    REST Controllers  |
+----------------------+
          |
          v
+----------------------+
|       Services       |
|   Business Logic     |
+----------------------+
          |
          v
+----------------------+
|     Repositories     |
|   Data Access (JPA)  |
+----------------------+
          |
          v
+----------------------+
|        MySQL         |
+----------------------+