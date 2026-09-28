package salary_management.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import salary_management.dto.EmployeeRequest;
import salary_management.dto.EmployeeResponse;
import salary_management.entity.Employee;
import salary_management.exception.EmployeeNotFoundException;
import salary_management.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void shouldGetEmployeeById() {

        Employee employee = Employee.builder()
                .id(1L)
                .employeeNumber("EMP00001")
                .firstName("John")
                .lastName("Doe")
                .email("john@acme.com")
                .department("Engineering")
                .country("India")
                .jobTitle("Software Engineer")
                .salary(BigDecimal.valueOf(1_000_000))
                .currency("INR")
                .build();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse response =
                employeeService.getEmployeeById(1L);

        assertEquals(1L, response.getId());
        assertEquals("EMP00001", response.getEmployeeNumber());
        assertEquals("John", response.getFirstName());
        assertEquals(BigDecimal.valueOf(1_000_000), response.getSalary());

        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(999L)
        );

        verify(employeeRepository).findById(999L);
    }

    @Test
    void shouldCreateEmployee() {

        EmployeeRequest request = EmployeeRequest.builder()
                .employeeNumber("EMP00001")
                .firstName("John")
                .lastName("Doe")
                .email("john@acme.com")
                .department("Engineering")
                .country("India")
                .jobTitle("Software Engineer")
                .salary(BigDecimal.valueOf(1_000_000))
                .currency("INR")
                .build();

        Employee savedEmployee = Employee.builder()
                .id(1L)
                .employeeNumber("EMP00001")
                .firstName("John")
                .lastName("Doe")
                .email("john@acme.com")
                .department("Engineering")
                .country("India")
                .jobTitle("Software Engineer")
                .salary(BigDecimal.valueOf(1_000_000))
                .currency("INR")
                .build();

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse response =
                employeeService.createEmployee(request);

        assertEquals(1L, response.getId());
        assertEquals("EMP00001", response.getEmployeeNumber());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void shouldUpdateEmployee() {
        Long employeeId = 1L;

        Employee existingEmployee = Employee.builder()
                .id(employeeId)
                .employeeNumber("EMP00001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@acme.com")
                .department("Engineering")
                .country("India")
                .jobTitle("Software Engineer")
                .salary(new BigDecimal("800000"))
                .currency("INR")
                .build();

        EmployeeRequest request = EmployeeRequest.builder()
                .employeeNumber("EMP00001")
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@acme.com")
                .department("Engineering")
                .country("India")
                .jobTitle("Senior Software Engineer")
                .salary(new BigDecimal("1000000"))
                .currency("INR")
                .build();

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponse response =
                employeeService.updateEmployee(employeeId, request);

        assertEquals("Smith", response.getLastName());
        assertEquals("Senior Software Engineer", response.getJobTitle());
        assertEquals(new BigDecimal("1000000"), response.getSalary());

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    void shouldDeleteEmployee() {
        Long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .employeeNumber("EMP00001")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@acme.com")
                .department("Engineering")
                .country("India")
                .jobTitle("Software Engineer")
                .salary(new BigDecimal("800000"))
                .currency("INR")
                .build();

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).delete(employee);
    }
}