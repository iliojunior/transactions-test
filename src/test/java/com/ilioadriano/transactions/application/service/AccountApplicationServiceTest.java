package com.ilioadriano.transactions.application.service;

import com.ilioadriano.transactions.TestUtils;
import com.ilioadriano.transactions.application.mapper.AccountMapperImpl;
import com.ilioadriano.transactions.domain.exception.DuplicateEntryException;
import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.service.AccountDomainService;
import com.ilioadriano.transactions.rest.dto.ErrorResponseDTO;
import com.ilioadriano.transactions.rest.dto.account.AccountCreationDTO;
import com.ilioadriano.transactions.rest.dto.account.AccountResponseDTO;
import com.ilioadriano.transactions.rest.exception.UnprocessableEntityRestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class AccountApplicationServiceTest {

    @InjectMocks
    private AccountApplicationService accountApplicationService;

    @Mock
    private AccountDomainService accountDomainService;

    @Spy
    private AccountMapperImpl accountMapper;

    @Test
    public void shouldCreateAccount() {
        // Setup
        String randomAccountNumber = TestUtils.generateRandomDocumentNumber();

        AccountCreationDTO accountDTO = new AccountCreationDTO();
        accountDTO.setDocumentNumber(randomAccountNumber);

        Account mockAccount = new Account();
        mockAccount.setId(1);
        mockAccount.setDocumentNumber(randomAccountNumber);

        when(accountDomainService.create(eq(randomAccountNumber))).thenReturn(mockAccount);

        // Action
        ResponseEntity<AccountResponseDTO> responseEntity = accountApplicationService.create(accountDTO);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(1);
        assertThat(responseEntity.getBody().getDocumentNumber()).isEqualTo(randomAccountNumber);
    }

    @Test
    public void shouldReturnAccountById() throws EntityNotFoundException {
        // Setup
        String randomAccountNumber = TestUtils.generateRandomDocumentNumber();

        Account mockCreatedAccount = new Account();
        mockCreatedAccount.setId(1);
        mockCreatedAccount.setDocumentNumber(randomAccountNumber);
        when(accountDomainService.findById(eq(1))).thenReturn(mockCreatedAccount);

        ResponseEntity<AccountResponseDTO> responseEntity = accountApplicationService.getAccountById(1);

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(1);
        assertThat(responseEntity.getBody().getDocumentNumber()).isEqualTo(randomAccountNumber);
    }

    @Test
    public void shouldReturnNotFoundIfEntityDoesNotExists() throws EntityNotFoundException {
        when(accountDomainService.findById(eq(1))).thenThrow(new EntityNotFoundException());

        ResponseEntity<AccountResponseDTO> responseEntity = accountApplicationService.getAccountById(1);

        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void shouldReturnUnprocessableIfDuplicatedEntry() throws DuplicateEntryException {
        when(accountDomainService.save(any())).thenThrow(new DuplicateEntryException());

        assertThatThrownBy(() -> accountApplicationService.create(new AccountCreationDTO()))
                .isInstanceOf(UnprocessableEntityRestException.class)
                .extracting(exception -> (UnprocessableEntityRestException) exception)
                .satisfies(exception -> {
                    ErrorResponseDTO errorResponseDTO = exception.getErrorResponseDTO();

                    assertThat(errorResponseDTO).isNotNull();
                    assertThat(errorResponseDTO.getErrors()).isNotNull();
                    assertThat(errorResponseDTO.getErrors()).hasSize(1);

                    ErrorResponseDTO.ErrorDTO errorDTO = errorResponseDTO.getErrors().getFirst();
                    assertThat(errorDTO.getCause()).isEqualTo("Duplicate entry");
                    assertThat(errorDTO.getMessage()).isEqualTo("Already exists an Account with this document number");
                });
    }

}