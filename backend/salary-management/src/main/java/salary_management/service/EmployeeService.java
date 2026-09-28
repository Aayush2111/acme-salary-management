package salary_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import salary_management.dto.*;

import java.util.List;

public interface EmployeeService {

    Page<EmployeeResponse> getAllEmployees(
            String search,
            String department,
            String country,
            Pageable pageable
    );

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);

    SalarySummaryResponse getSalarySummary();

    List<DepartmentSalaryResponse> getSalaryByDepartment();

    List<CountrySalaryResponse> getSalaryByCountry();

    List<SalaryDistributionResponse> getSalaryDistribution();
}
