package salary_management.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import salary_management.dto.EmployeeRequest;
import salary_management.dto.EmployeeResponse;
import salary_management.exception.EmployeeNotFoundException;
import salary_management.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void shouldGetEmployeeById() throws Exception {

        EmployeeResponse response = EmployeeResponse.builder()
                .id(1L)
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

        when(employeeService.getEmployeeById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeNumber").value("EMP00001"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.department").value("Engineering"));
    }
}