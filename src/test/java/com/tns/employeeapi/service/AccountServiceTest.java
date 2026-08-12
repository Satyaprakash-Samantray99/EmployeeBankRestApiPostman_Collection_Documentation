package com.tns.employeeapi.service;

import com.tns.employeeapi.dto.AccountBalanceResponse;
import com.tns.employeeapi.dto.BalanceResponse;
import com.tns.employeeapi.dto.DepositRequest;
import com.tns.employeeapi.dto.WithdrawRequest;
import com.tns.employeeapi.entity.Account;
import com.tns.employeeapi.exception.BusinessException;
import com.tns.employeeapi.exception.ResourceNotFoundException;
import com.tns.employeeapi.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountService Unit Tests")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account sampleAccount;
    private DepositRequest depositRequest;
    private WithdrawRequest withdrawRequest;

    @BeforeEach
    void setUp() {

        sampleAccount = new Account();
        sampleAccount.setId(1);
        sampleAccount.setAccountNumber("ACC123456");
        sampleAccount.setAccountHolderName("Satya Prakash");
        sampleAccount.setBalance(1000.0);

        depositRequest = new DepositRequest();
        depositRequest.setAmount(500.0);

        withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAmount(300.0);
    }

    @Nested
    @DisplayName("getBalance Tests")
    class GetBalanceTests {

        @Test
        @DisplayName("Should return balance when account exists")
        void givenValidId_whenGetBalance_thenReturnBalanceResponse() {

            given(accountRepository.findById(1))
                    .willReturn(Optional.of(sampleAccount));

            AccountBalanceResponse response = accountService.getBalance(1);

            assertThat(response).isNotNull();
            assertThat(response.getBalance()).isEqualTo(1000.0);

            verify(accountRepository).findById(1);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when account does not exist")
        void givenInvalidId_whenGetBalance_thenThrowResourceNotFoundException() {

            given(accountRepository.findById(99))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.getBalance(99))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Account not found.");

            verify(accountRepository).findById(99);
        }
    }

    @Nested
    @DisplayName("Deposit Tests")
    class DepositTests {

        @Test
        @DisplayName("Should deposit amount successfully")
        void givenValidId_whenDeposit_thenReturnUpdatedBalance() {

            given(accountRepository.findById(1))
                    .willReturn(Optional.of(sampleAccount));

            given(accountRepository.save(any(Account.class)))
                    .willReturn(sampleAccount);

            BalanceResponse response =
                    accountService.deposit(1, depositRequest);

            assertThat(response).isNotNull();
            assertThat(response.getBalance()).isEqualTo(1500.0);

            verify(accountRepository).findById(1);
            verify(accountRepository).save(sampleAccount);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when account not found")
        void givenInvalidId_whenDeposit_thenThrowResourceNotFoundException() {

            given(accountRepository.findById(99))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() ->
                    accountService.deposit(99, depositRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Account not found.");

            verify(accountRepository).findById(99);
            verify(accountRepository, never()).save(any(Account.class));
        }
    }

    @Nested
    @DisplayName("Withdraw Tests")
    class WithdrawTests {

        @Test
        @DisplayName("Should withdraw amount successfully")
        void givenValidId_whenWithdraw_thenReturnUpdatedBalance() {

            given(accountRepository.findById(1))
                    .willReturn(Optional.of(sampleAccount));

            given(accountRepository.save(any(Account.class)))
                    .willReturn(sampleAccount);

            BalanceResponse response =
                    accountService.withdraw(1, withdrawRequest);

            assertThat(response).isNotNull();
            assertThat(response.getBalance()).isEqualTo(700.0);

            verify(accountRepository).findById(1);
            verify(accountRepository).save(sampleAccount);
        }

        @Test
        @DisplayName("Should throw BusinessException when balance is insufficient")
        void givenInsufficientBalance_whenWithdraw_thenThrowBusinessException() {

            withdrawRequest.setAmount(2000.0);

            given(accountRepository.findById(1))
                    .willReturn(Optional.of(sampleAccount));

            assertThatThrownBy(() ->
                    accountService.withdraw(1, withdrawRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("Insufficient balance.");

            verify(accountRepository).findById(1);
            verify(accountRepository, never()).save(any(Account.class));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when account does not exist")
        void givenInvalidId_whenWithdraw_thenThrowResourceNotFoundException() {

            given(accountRepository.findById(99))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() ->
                    accountService.withdraw(99, withdrawRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Account not found.");

            verify(accountRepository).findById(99);
            verify(accountRepository, never()).save(any(Account.class));
        }
    }
}