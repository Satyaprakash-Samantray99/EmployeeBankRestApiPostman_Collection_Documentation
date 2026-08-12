package com.tns.employeeapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tns.employeeapi.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByAccountNumber(String accountNumber);

}