package salary_management.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_management.dto.EmployeeRequest;
import salary_management.dto.EmployeeResponse;
import salary_management.entity.Employee;
import salary_management.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + id));

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
                        new RuntimeException("Employee not found with id: " + id));

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
                        new RuntimeException("Employee not found with id: " + id));

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
}