package salary_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import salary_management.dto.CountrySalaryResponse;
import salary_management.dto.DepartmentSalaryResponse;
import salary_management.entity.Employee;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    @Query("""
        SELECT COUNT(e)
        FROM Employee e
        """)
    long countEmployees();

    @Query("""
        SELECT AVG(e.salary)
        FROM Employee e
        """)
    BigDecimal findAverageSalary();

    @Query("""
        SELECT MIN(e.salary)
        FROM Employee e
        """)
    BigDecimal findMinimumSalary();

    @Query("""
        SELECT MAX(e.salary)
        FROM Employee e
        """)
    BigDecimal findMaximumSalary();

    @Query("""
        SELECT new salary_management.dto.DepartmentSalaryResponse(
            e.department,
            COUNT(e),
            AVG(e.salary)
        )
        FROM Employee e
        GROUP BY e.department
        ORDER BY AVG(e.salary) DESC
        """)
    List<DepartmentSalaryResponse> findSalaryByDepartment();

    @Query("""
        SELECT new salary_management.dto.CountrySalaryResponse(
            e.country,
            COUNT(e),
            AVG(e.salary)
        )
        FROM Employee e
        GROUP BY e.country
        ORDER BY AVG(e.salary) DESC
        """)
    List<CountrySalaryResponse> findSalaryByCountry();

    @Query("""
        SELECT e.salary
        FROM Employee e
        """)
    List<BigDecimal> findAllSalaries();
}
