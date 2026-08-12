package com.tns.employeeapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tns.employeeapi.dto.AccountBalanceResponse;
import com.tns.employeeapi.dto.BalanceResponse;
import com.tns.employeeapi.dto.DepositRequest;
import com.tns.employeeapi.dto.WithdrawRequest;
import com.tns.employeeapi.entity.Account;
import com.tns.employeeapi.exception.BusinessException;
import com.tns.employeeapi.exception.ResourceNotFoundException;
import com.tns.employeeapi.repository.AccountRepository;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Cacheable(value = "accountBalances", key = "#id")
    @Transactional(readOnly = true)
    public AccountBalanceResponse getBalance(Integer id) {
        LOGGER.info("Fetching account balance for id: {} from Database", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        return new AccountBalanceResponse(account.getBalance());
    }

    @CachePut(value = "accounts", key = "#id")
    @CacheEvict(value = "accountBalances", key = "#id")
    @Transactional
    public BalanceResponse deposit(Integer id, DepositRequest request) {
        LOGGER.info("Depositing {} into account id: {}", request.getAmount(), id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        account.setBalance(account.getBalance() + request.getAmount());
        Account updatedAccount = accountRepository.save(account);

        return mapToBalanceResponse(updatedAccount);
    }

    @CachePut(value = "accounts", key = "#id")
    @CacheEvict(value = "accountBalances", key = "#id")
    @Transactional
    public BalanceResponse withdraw(Integer id, WithdrawRequest request) {
        LOGGER.info("Withdrawing {} from account id: {}", request.getAmount(), id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        if (account.getBalance() < request.getAmount()) {
            throw new BusinessException("Insufficient balance.");
        }

        account.setBalance(account.getBalance() - request.getAmount());
        Account updatedAccount = accountRepository.save(account);

        return mapToBalanceResponse(updatedAccount);
    }

    private BalanceResponse mapToBalanceResponse(Account account) {
        return new BalanceResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountHolderName(),
                account.getBalance());
    }
}