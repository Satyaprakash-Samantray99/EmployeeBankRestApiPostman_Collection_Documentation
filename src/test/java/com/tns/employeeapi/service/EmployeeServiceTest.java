package com.tns.employeeapi.service;

import com.tns.employeeapi.dto.EmployeeRequest;
import com.tns.employeeapi.dto.EmployeeResponse;
import com.tns.employeeapi.entity.Employee;
import com.tns.employeeapi.exception.BusinessException;
import com.tns.employeeapi.exception.DuplicateResourceException;
import com.tns.employeeapi.exception.ResourceNotFoundException;
import com.tns.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Unit Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeRequest validRequest;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setName("Satya Prakash");
        employee.setEmail("satya@example.com");
        employee.setDepartment("Engineering");
        employee.setSalary(5000.0);

        validRequest = new EmployeeRequest();
        validRequest.setName("Satya Prakash");
        validRequest.setEmail("satya@example.com");
        validRequest.setDepartment("Engineering");
        validRequest.setSalary(5000.0);
    }

    
    @Nested
    @DisplayName("getEmployeeById Tests")
    class GetEmployeeByIdTests {

        @Test
        @DisplayName("Should return EmployeeResponse when employee exists")
        void givenValidId_whenGetEmployeeById_thenReturnEmployeeResponse() {
            // ARRANGE
            given(employeeRepository.findById(1)).willReturn(Optional.of(employee));

            // ACT
            EmployeeResponse response = employeeService.getEmployeeById(1);

            // ASSERT
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1);
            assertThat(response.getName()).isEqualTo("Satya Prakash");
            assertThat(response.getEmail()).isEqualTo("satya@example.com");
            assertThat(response.getDepartment()).isEqualTo("Engineering");
            assertThat(response.getSalary()).isEqualTo(5000.0);

            verify(employeeRepository).findById(1);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when employee does not exist")
        void givenNonExistentId_whenGetEmployeeById_thenThrowResourceNotFoundException() {
            // ARRANGE
            given(employeeRepository.findById(99)).willReturn(Optional.empty());

            // ACT & ASSERT
            assertThatThrownBy(() -> employeeService.getEmployeeById(99))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Employee not found.");

            verify(employeeRepository).findById(99);
        }
    }

    
    @Nested
    @DisplayName("addEmployee Tests")
    class AddEmployeeTests {

        @Test
        @DisplayName("Should successfully create and return new employee")
        void givenValidRequest_whenAddEmployee_thenReturnSavedEmployeeResponse() {
            // ARRANGE
            given(employeeRepository.existsByEmail(validRequest.getEmail())).willReturn(false);
            given(employeeRepository.save(any(Employee.class))).willReturn(employee);

            // ACT
            EmployeeResponse response = employeeService.addEmployee(validRequest);

            // ASSERT
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1);
            assertThat(response.getEmail()).isEqualTo(validRequest.getEmail());

            ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
            verify(employeeRepository).save(employeeCaptor.capture());
            Employee capturedEmployee = employeeCaptor.getValue();
            assertThat(capturedEmployee.getName()).isEqualTo(validRequest.getName());
            assertThat(capturedEmployee.getSalary()).isEqualTo(validRequest.getSalary());
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when email already exists")
        void givenDuplicateEmail_whenAddEmployee_thenThrowDuplicateResourceException() {
            // ARRANGE
            given(employeeRepository.existsByEmail(validRequest.getEmail())).willReturn(true);

            // ACT & ASSERT
            assertThatThrownBy(() -> employeeService.addEmployee(validRequest))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessage("Employee with this email already exists.");

            verify(employeeRepository, never()).save(any(Employee.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when salary is less than 1000")
        void givenLowSalary_whenAddEmployee_thenThrowBusinessException() {
            // ARRANGE
            validRequest.setSalary(999.0);
            given(employeeRepository.existsByEmail(validRequest.getEmail())).willReturn(false);

            // ACT & ASSERT
            assertThatThrownBy(() -> employeeService.addEmployee(validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("Salary must be greater than or equal to 1000.");

            verify(employeeRepository, never()).save(any(Employee.class));
        }
    }

    
    @Nested
    @DisplayName("updateEmployee Tests")
    class UpdateEmployeeTests {

        @Test
        @DisplayName("Should successfully update employee when valid data provided")
        void givenValidIdAndRequest_whenUpdateEmployee_thenReturnUpdatedEmployeeResponse() {
            // ARRANGE
            given(employeeRepository.findById(1)).willReturn(Optional.of(employee));
            given(employeeRepository.findByEmail(validRequest.getEmail())).willReturn(Optional.of(employee));
            given(employeeRepository.save(any(Employee.class))).willReturn(employee);

            // ACT
            EmployeeResponse response = employeeService.updateEmployee(1, validRequest);

            // ASSERT
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1);
            verify(employeeRepository).save(employee);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when updating non-existent employee")
        void givenNonExistentId_whenUpdateEmployee_thenThrowResourceNotFoundException() {
            // ARRANGE
            given(employeeRepository.findById(99)).willReturn(Optional.empty());

            // ACT & ASSERT
            assertThatThrownBy(() -> employeeService.updateEmployee(99, validRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Employee not found.");

            verify(employeeRepository, never()).save(any(Employee.class));
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when updating email to one belonging to another employee")
        void givenEmailBelongingToAnotherEmployee_whenUpdateEmployee_thenThrowDuplicateResourceException() {
            // ARRANGE
            Employee otherEmployee = new Employee();
            otherEmployee.setId(2);
            otherEmployee.setEmail(validRequest.getEmail());

            given(employeeRepository.findById(1)).willReturn(Optional.of(employee));
            given(employeeRepository.findByEmail(validRequest.getEmail())).willReturn(Optional.of(otherEmployee));

            // ACT & ASSERT
            assertThatThrownBy(() -> employeeService.updateEmployee(1, validRequest))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessage("Employee with this email already exists.");

            verify(employeeRepository, never()).save(any(Employee.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when updating salary to less than 1000")
        void givenLowSalary_whenUpdateEmployee_thenThrowBusinessException() {
            // ARRANGE
            validRequest.setSalary(500.0);
            given(employeeRepository.findById(1)).willReturn(Optional.of(employee));
            given(employeeRepository.findByEmail(validRequest.getEmail())).willReturn(Optional.of(employee));

            // ACT & ASSERT
            assertThatThrownBy(() -> employeeService.updateEmployee(1, validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("Salary must be greater than or equal to 1000.");

            verify(employeeRepository, never()).save(any(Employee.class));
        }
    }

    
    
    @Nested
    @DisplayName("deleteEmployee Tests")
    class DeleteEmployeeTests {

        @Test
        @DisplayName("Should delete employee when ID exists")
        void givenValidId_whenDeleteEmployee_thenDeleteFromRepository() {
            // ARRANGE
            given(employeeRepository.findById(1)).willReturn(Optional.of(employee));

            // ACT
            employeeService.deleteEmployee(1);

            // ASSERT
            verify(employeeRepository).findById(1);
            verify(employeeRepository).delete(employee);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when deleting non-existent ID")
        void givenNonExistentId_whenDeleteEmployee_thenThrowResourceNotFoundException() {
            // ARRANGE
            given(employeeRepository.findById(99)).willReturn(Optional.empty());

            // ACT & ASSERT
            assertThatThrownBy(() -> employeeService.deleteEmployee(99))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Employee not found.");

            verify(employeeRepository, never()).delete(any(Employee.class));
        }
    }

   
    @Nested
    @DisplayName("getAllEmployees Tests")
    class GetAllEmployeesTests {

        @Test
        @DisplayName("Should return paged EmployeeResponse list")
        void givenPageable_whenGetAllEmployees_thenReturnMappedPage() {
            // ARRANGE
            Pageable pageable = PageRequest.of(0, 10);
            Page<Employee> employeePage = new PageImpl<>(List.of(employee));
            given(employeeRepository.findAll(pageable)).willReturn(employeePage);

            // ACT
            Page<EmployeeResponse> result = employeeService.getAllEmployees(pageable);

            // ASSERT
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("Satya Prakash");
            verify(employeeRepository).findAll(pageable);
        }
    }
}