package salary_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salary_management.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}