package salary_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CountrySalaryResponse {

    private String country;
    private Long employeeCount;
    private Double averageSalary;
}