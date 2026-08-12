package com.tns.employeeapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tns.employeeapi.dto.EmployeeRequest;
import com.tns.employeeapi.dto.EmployeeResponse;
import com.tns.employeeapi.entity.Employee;
import com.tns.employeeapi.exception.BusinessException;
import com.tns.employeeapi.exception.DuplicateResourceException;
import com.tns.employeeapi.exception.ResourceNotFoundException;
import com.tns.employeeapi.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Read: Fetches from database on first call, returns from Redis cache on subsequent calls
    @Cacheable(value = "employees", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Integer id) {
        LOGGER.info("Fetching employee with id: {} from Database", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        return mapToEmployeeResponse(employee);
    }

    // Create: Evicts any active page caches so list queries pick up fresh data
    @CacheEvict(value = "employees", allEntries = true)
    @Transactional
    public EmployeeResponse addEmployee(EmployeeRequest request) {
        LOGGER.info("Creating employee with email: {}", request.getEmail());

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee with this email already exists.");
        }

        if (request.getSalary() < 1000) {
            throw new BusinessException("Salary must be greater than or equal to 1000.");
        }

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToEmployeeResponse(savedEmployee);
    }

    // Update: Uses @CachePut to update the Redis cache entry directly with the new state
    @CachePut(value = "employees", key = "#id")
    @Transactional
    public EmployeeResponse updateEmployee(Integer id, EmployeeRequest request) {
        LOGGER.info("Updating employee with id: {} in Database and Cache", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        employeeRepository.findByEmail(request.getEmail())
                .ifPresent(existingEmployee -> {
                    if (!existingEmployee.getId().equals(id)) {
                        throw new DuplicateResourceException("Employee with this email already exists.");
                    }
                });

        if (request.getSalary() < 1000) {
            throw new BusinessException("Salary must be greater than or equal to 1000.");
        }

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToEmployeeResponse(updatedEmployee);
    }

    // Delete: Removes the specific key from Redis cache upon deletion
    @CacheEvict(value = "employees", key = "#id")
    @Transactional
    public void deleteEmployee(Integer id) {
        LOGGER.info("Deleting employee with id: {} from Database and Cache", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        employeeRepository.delete(employee);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::mapToEmployeeResponse);
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary());
    }
}