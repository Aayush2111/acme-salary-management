package salary_management.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import salary_management.dto.*;
import salary_management.entity.Employee;
import salary_management.repository.EmployeeRepository;
import salary_management.exception.EmployeeNotFoundException;
import salary_management.specification.EmployeeSpecification;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Page<EmployeeResponse> getAllEmployees(
            String search,
            String department,
            String country,
            Pageable pageable) {

        Specification<Employee> specification = (root, query, criteriaBuilder) -> null;

        if (search != null && !search.isBlank()) {
            specification = specification.and(
                    EmployeeSpecification.hasSearchTerm(search)
            );
        }

        if (department != null && !department.isBlank()) {
            specification = specification.and(
                    EmployeeSpecification.hasDepartment(department)
            );
        }

        if (country != null && !country.isBlank()) {
            specification = specification.and(
                    EmployeeSpecification.hasCountry(country)
            );
        }

        return employeeRepository.findAll(specification, pageable)
                .map(this::toResponse);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        return toResponse(employee);
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = toEntity(request);
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        employee.setEmployeeNumber(request.getEmployeeNumber());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setCountry(request.getCountry());
        employee.setJobTitle(request.getJobTitle());
        employee.setSalary(request.getSalary());
        employee.setCurrency(request.getCurrency());

        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }

    private Employee toEntity(EmployeeRequest request) {
        return Employee.builder()
                .employeeNumber(request.getEmployeeNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .country(request.getCountry())
                .jobTitle(request.getJobTitle())
                .salary(request.getSalary())
                .currency(request.getCurrency())
                .build();
    }

    private EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeNumber(employee.getEmployeeNumber())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .country(employee.getCountry())
                .jobTitle(employee.getJobTitle())
                .salary(employee.getSalary())
                .currency(employee.getCurrency())
                .build();
    }

    @Override
    public SalarySummaryResponse getSalarySummary() {

        return SalarySummaryResponse.builder()
                .totalEmployees(employeeRepository.countEmployees())
                .averageSalary(employeeRepository.findAverageSalary())
                .minimumSalary(employeeRepository.findMinimumSalary())
                .maximumSalary(employeeRepository.findMaximumSalary())
                .build();
    }

    @Override
    public List<DepartmentSalaryResponse> getSalaryByDepartment() {
        return employeeRepository.findSalaryByDepartment();
    }

    @Override
    public List<CountrySalaryResponse> getSalaryByCountry() {
        return employeeRepository.findSalaryByCountry();
    }

    @Override
    public List<SalaryDistributionResponse> getSalaryDistribution() {

        List<BigDecimal> salaries = employeeRepository.findAllSalaries();

        long below500k = 0;
        long between500kAnd1m = 0;
        long between1mAnd1_5m = 0;
        long between1_5mAnd2m = 0;
        long above2m = 0;

        for (BigDecimal salary : salaries) {

            if (salary.compareTo(BigDecimal.valueOf(500_000)) < 0) {
                below500k++;
            } else if (salary.compareTo(BigDecimal.valueOf(1_000_000)) < 0) {
                between500kAnd1m++;
            } else if (salary.compareTo(BigDecimal.valueOf(1_500_000)) < 0) {
                between1mAnd1_5m++;
            } else if (salary.compareTo(BigDecimal.valueOf(2_000_000)) < 0) {
                between1_5mAnd2m++;
            } else {
                above2m++;
            }
        }

        return List.of(
                SalaryDistributionResponse.builder()
                        .salaryRange("< 500K")
                        .employeeCount(below500k)
                        .build(),

                SalaryDistributionResponse.builder()
                        .salaryRange("500K - 999K")
                        .employeeCount(between500kAnd1m)
                        .build(),

                SalaryDistributionResponse.builder()
                        .salaryRange("1M - 1.49M")
                        .employeeCount(between1mAnd1_5m)
                        .build(),

                SalaryDistributionResponse.builder()
                        .salaryRange("1.5M - 1.99M")
                        .employeeCount(between1_5mAnd2m)
                        .build(),

                SalaryDistributionResponse.builder()
                        .salaryRange("2M+")
                        .employeeCount(above2m)
                        .build()
        );
    }
}