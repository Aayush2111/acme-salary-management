package salary_management.dto;

import jakarta.validation.constraints.*;
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
public class EmployeeRequest {

    @NotBlank
    private String employeeNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String department;

    @NotBlank
    private String country;

    @NotBlank
    private String jobTitle;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal salary;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;
}