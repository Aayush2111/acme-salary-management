package salary_management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import salary_management.dto.CountrySalaryResponse;
import salary_management.dto.DepartmentSalaryResponse;
import salary_management.dto.SalaryDistributionResponse;
import salary_management.dto.SalarySummaryResponse;
import salary_management.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final EmployeeService employeeService;

    @GetMapping("/summary")
    public SalarySummaryResponse getSalarySummary() {
        return employeeService.getSalarySummary();
    }

    @GetMapping("/by-department")
    public List<DepartmentSalaryResponse> getSalaryByDepartment() {
        return employeeService.getSalaryByDepartment();
    }

    @GetMapping("/by-country")
    public List<CountrySalaryResponse> getSalaryByCountry() {
        return employeeService.getSalaryByCountry();
    }

    @GetMapping("/distribution")
    public List<SalaryDistributionResponse> getSalaryDistribution() {
        return employeeService.getSalaryDistribution();
    }
}