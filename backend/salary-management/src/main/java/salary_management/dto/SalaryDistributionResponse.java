package salary_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SalaryDistributionResponse {

    private String salaryRange;
    private Long employeeCount;
}