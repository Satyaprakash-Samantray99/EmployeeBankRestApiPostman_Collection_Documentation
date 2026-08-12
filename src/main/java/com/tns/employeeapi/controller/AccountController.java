package com.tns.employeeapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tns.employeeapi.dto.AccountBalanceResponse;
import com.tns.employeeapi.dto.BalanceResponse;
import com.tns.employeeapi.dto.DepositRequest;
import com.tns.employeeapi.dto.WithdrawRequest;
import com.tns.employeeapi.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Deposit amount into account")
    @PostMapping("/{id}/deposit")
    public ResponseEntity<BalanceResponse> deposit(
            @PathVariable Integer id,
            @Valid @RequestBody DepositRequest request) {

        LOGGER.info("Received deposit request for account id: {}", id);

        return ResponseEntity.ok(accountService.deposit(id, request));
    }

    @Operation(summary = "Withdraw amount from account")
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BalanceResponse> withdraw(
            @PathVariable Integer id,
            @Valid @RequestBody WithdrawRequest request) {

        LOGGER.info("Received withdraw request for account id: {}", id);

        return ResponseEntity.ok(accountService.withdraw(id, request));
    }

    @Operation(summary = "Get account balance")
    @GetMapping("/{id}/balance")
    public ResponseEntity<AccountBalanceResponse> getBalance(
            @PathVariable Integer id) {

        LOGGER.info("Received balance request for account id: {}", id);

        return ResponseEntity.ok(accountService.getBalance(id));
    }
}