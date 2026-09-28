package salary_management.config;

import lombok.RequiredArgsConstructor;
import salary_management.entity.Employee;
import salary_management.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final int EMPLOYEE_COUNT = 10_000;
    private static final int BATCH_SIZE = 500;

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {

        if (employeeRepository.count() > 0) {
            return;
        }

        List<Employee> employees = new ArrayList<>(BATCH_SIZE);

        for (int i = 1; i <= EMPLOYEE_COUNT; i++) {

            employees.add(Employee.builder()
                    .employeeNumber(String.format("EMP%05d", i))
                    .firstName("FirstName" + i)
                    .lastName("LastName" + i)
                    .email("employee" + i + "@acme.com")
                    .department(getDepartment(i))
                    .country(getCountry(i))
                    .jobTitle(getJobTitle(i))
                    .salary(getSalary(i))
                    .currency("INR")
                    .build());

            if (employees.size() == BATCH_SIZE) {
                employeeRepository.saveAll(employees);
                employees.clear();
            }
        }

        if (!employees.isEmpty()) {
            employeeRepository.saveAll(employees);
        }
    }

    private String getDepartment(int index) {
        String[] departments = {
                "Engineering",
                "Finance",
                "Human Resources",
                "Sales",
                "Marketing",
                "Operations",
                "Legal",
                "IT"
        };

        return departments[(index - 1) % departments.length];
    }

    private String getCountry(int index) {
        String[] countries = {
                "India",
                "United States",
                "United Kingdom",
                "Canada",
                "Germany",
                "Australia"
        };

        return countries[(index - 1) % countries.length];
    }

    private String getJobTitle(int index) {
        String[] jobTitles = {
                "Software Engineer",
                "Senior Software Engineer",
                "Manager",
                "Business Analyst",
                "HR Specialist",
                "Sales Executive",
                "Financial Analyst",
                "Product Manager"
        };

        return jobTitles[(index - 1) % jobTitles.length];
    }

    private BigDecimal getSalary(int index) {
        return BigDecimal.valueOf(
                500_000L + ((index * 73_000L) % 2_500_000L)
        );
    }
}