package com.tns.employeeapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tns.employeeapi.dto.EmployeeRequest;
import com.tns.employeeapi.exception.BusinessException;
import com.tns.employeeapi.exception.DuplicateResourceException;
import com.tns.employeeapi.exception.GlobalExceptionHandler;
import com.tns.employeeapi.exception.ResourceNotFoundException;
import com.tns.employeeapi.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn404WhenEmployeeNotFound() throws Exception {

        doThrow(new ResourceNotFoundException("Employee not found."))
                .when(employeeService)
                .deleteEmployee(1);

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Requested resource not found."));
    }

    @Test
    void shouldReturn409WhenDuplicateEmailExists() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Satya");
        request.setEmail("satya@gmail.com");
        request.setDepartment("IT");
        request.setSalary(50000.0);

        when(employeeService.addEmployee(any(EmployeeRequest.class)))
                .thenThrow(new DuplicateResourceException(
                        "Employee with this email already exists."));

        mockMvc.perform(post("/api/v1/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldReturn422ForBusinessException() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Satya");
        request.setEmail("satya@gmail.com");
        request.setDepartment("IT");
        request.setSalary(500.0);

        when(employeeService.addEmployee(any(EmployeeRequest.class)))
                .thenThrow(new BusinessException(
                        "Salary must be greater than or equal to 1000."));

        mockMvc.perform(post("/api/v1/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setName("");
        request.setEmail("abc");
        request.setDepartment("");
        request.setSalary(-10.0);

        mockMvc.perform(post("/api/v1/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn500ForUnexpectedException() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setName("Satya");
        request.setEmail("satya@gmail.com");
        request.setDepartment("IT");
        request.setSalary(50000.0);

        when(employeeService.addEmployee(any(EmployeeRequest.class)))
                .thenThrow(new RuntimeException("Database down"));

        mockMvc.perform(post("/api/v1/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message")
                        .value("An unexpected error occurred. Please try again later."));
    }
}