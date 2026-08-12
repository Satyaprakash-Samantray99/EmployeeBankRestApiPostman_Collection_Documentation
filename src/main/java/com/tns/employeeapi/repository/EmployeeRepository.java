package com.tns.employeeapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tns.employeeapi.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

}
