package salary_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String country;
    private String jobTitle;
    private BigDecimal salary;
    private String currency;
}