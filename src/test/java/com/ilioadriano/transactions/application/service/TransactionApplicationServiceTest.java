package com.ilioadriano.transactions.application.service;

import com.ilioadriano.transactions.TestUtils;
import com.ilioadriano.transactions.application.mapper.TransactionMapperImpl;
import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.model.OperationType;
import com.ilioadriano.transactions.domain.model.OperationTypeEnum;
import com.ilioadriano.transactions.domain.model.Transaction;
import com.ilioadriano.transactions.domain.service.AccountDomainService;
import com.ilioadriano.transactions.domain.service.OperationTypeDomainService;
import com.ilioadriano.transactions.domain.service.TransactionDomainService;
import com.ilioadriano.transactions.rest.dto.ErrorResponseDTO;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionCreationDTO;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionResponseDTO;
import com.ilioadriano.transactions.rest.exception.UnprocessableEntityRestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionApplicationServiceTest {

    @InjectMocks
    private TransactionApplicationService transactionApplicationService;

    @Spy
    private TransactionMapperImpl transactionMapper;

    @Mock
    private TransactionDomainService transactionDomainService;

    @Mock
    private AccountDomainService accountDomainService;

    @Mock
    private OperationTypeDomainService operationTypeDomainService;

    @Test
    void shouldThrowExceptionIfNotFoundAccount() throws EntityNotFoundException {
        when(accountDomainService.findById(any())).thenThrow(new EntityNotFoundException());

        TransactionCreationDTO transactionCreationDTO = new TransactionCreationDTO();
        transactionCreationDTO.setAccountId(1);

        assertThatThrownBy(() -> transactionApplicationService.create(transactionCreationDTO))
                .isInstanceOf(UnprocessableEntityRestException.class)
                .extracting(exception -> (UnprocessableEntityRestException) exception)
                .satisfies(exception -> {
                    ErrorResponseDTO errorResponseDTO = exception.getErrorResponseDTO();

                    assertThat(errorResponseDTO).isNotNull();
                    assertThat(errorResponseDTO.getErrors()).isNotNull();
                    assertThat(errorResponseDTO.getErrors()).hasSize(1);

                    ErrorResponseDTO.ErrorDTO errorDTO = errorResponseDTO.getErrors().getFirst();
                    assertThat(errorDTO.getCause()).isEqualTo("Entity not found");
                    assertThat(errorDTO.getMessage()).isEqualTo("Account not found with id 1");
                });
    }

    @Test
    void shouldThrowExceptionIfNotFoundOperationType() throws EntityNotFoundException {
        when(operationTypeDomainService.findById(any())).thenThrow(new EntityNotFoundException());

        TransactionCreationDTO transactionCreationDTO = new TransactionCreationDTO();
        transactionCreationDTO.setOperationTypeId(1);

        assertThatThrownBy(() -> transactionApplicationService.create(transactionCreationDTO))
                .isInstanceOf(UnprocessableEntityRestException.class)
                .extracting(exception -> (UnprocessableEntityRestException) exception)
                .satisfies(exception -> {
                    ErrorResponseDTO errorResponseDTO = exception.getErrorResponseDTO();

                    assertThat(errorResponseDTO).isNotNull();
                    assertThat(errorResponseDTO.getErrors()).isNotNull();
                    assertThat(errorResponseDTO.getErrors()).hasSize(1);

                    ErrorResponseDTO.ErrorDTO errorDTO = errorResponseDTO.getErrors().getFirst();
                    assertThat(errorDTO.getCause()).isEqualTo("Entity not found");
                    assertThat(errorDTO.getMessage()).isEqualTo("OperationType not found with id 1");
                });
    }

    @Test
    void shouldCreateTransaction() throws EntityNotFoundException {
        Account account = new Account();
        OperationType operationType = new OperationType();
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationType(operationType);

        when(accountDomainService.findById(eq(1))).thenReturn(account);
        when(operationTypeDomainService.findById(eq(1))).thenReturn(operationType);
        when(transactionDomainService.createTransaction(eq(account), eq(operationType), eq(BigDecimal.TEN))).thenReturn(transaction);

        TransactionCreationDTO transactionCreationDTO = new TransactionCreationDTO();
        transactionCreationDTO.setAccountId(1);
        transactionCreationDTO.setOperationTypeId(1);
        transactionCreationDTO.setAmount(BigDecimal.TEN);

        ResponseEntity<TransactionResponseDTO> responseEntity = transactionApplicationService.create(transactionCreationDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getAccount()).isNotNull();
        assertThat(responseEntity.getBody().getOperationType()).isNotNull();
    }
}