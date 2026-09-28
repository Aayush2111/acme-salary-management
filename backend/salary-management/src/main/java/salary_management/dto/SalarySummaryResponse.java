package salary_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class SalarySummaryResponse {

    private long totalEmployees;
    private BigDecimal averageSalary;
    private BigDecimal minimumSalary;
    private BigDecimal maximumSalary;
}