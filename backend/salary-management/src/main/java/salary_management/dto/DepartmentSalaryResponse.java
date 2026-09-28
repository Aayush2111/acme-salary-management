package salary_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class DepartmentSalaryResponse {

    private String department;
    private Long employeeCount;
    private Double averageSalary;
}