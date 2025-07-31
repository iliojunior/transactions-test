package com.ilioadriano.transactions.domain.service;

import com.ilioadriano.transactions.TestUtils;
import com.ilioadriano.transactions.domain.exception.DuplicateEntryException;
import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountDomainServiceTest {

    @InjectMocks
    private AccountDomainService accountDomainService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void shouldCreateAccount() {
        String randomAccountNumber = TestUtils.generateRandomDocumentNumber();

        Account account = accountDomainService.create(randomAccountNumber);

        assertThat(account).isNotNull();
        assertThat(account.getDocumentNumber()).isEqualTo(randomAccountNumber);
    }

    @Test
    void shouldSaveAccount() throws DuplicateEntryException {
        Account account = new Account();
        Account mockAccount = new Account();

        when(accountRepository.saveAndFlush(any())).thenReturn(mockAccount);

        Account savedAccount = accountDomainService.save(account);

        verify(accountRepository).saveAndFlush(eq(account));
        assertThat(savedAccount).isEqualTo(mockAccount);
    }

    @Test
    void shouldFindAccountById() throws EntityNotFoundException {
        String randomAccountNumber = TestUtils.generateRandomDocumentNumber();

        Account mockCreatedAccount = new Account();
        mockCreatedAccount.setId(1);
        mockCreatedAccount.setDocumentNumber(randomAccountNumber);

        when(accountRepository.findById(eq(1))).thenReturn(Optional.of(mockCreatedAccount));

        Account resultAccount = accountDomainService.findById(1);

        assertThat(resultAccount).isNotNull();
        assertThat(resultAccount.getId()).isEqualTo(1);
        assertThat(resultAccount.getDocumentNumber()).isEqualTo(randomAccountNumber);
    }

    @Test
    void shouldThrownIfEntityDoesNotExists() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountDomainService.findById(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Account with id 1 was not found.");
    }

    @Test
    void shouldThrownIfDocumentNumberDuplicate() {
        when(accountRepository.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("Duplicate entry ..."));

        assertThatThrownBy(() -> accountDomainService.save(new Account()))
                .isInstanceOf(DuplicateEntryException.class);
    }

    @Test
    void shouldThrownIfUnknownIntegrityException() {
        when(accountRepository.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("Some other message"));

        assertThatThrownBy(() -> accountDomainService.save(new Account()))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Some other message");
    }

}
