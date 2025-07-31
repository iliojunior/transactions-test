package com.ilioadriano.transactions.domain.service;

import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.model.OperationType;
import com.ilioadriano.transactions.domain.model.OperationTypeEnum;
import com.ilioadriano.transactions.domain.model.Transaction;
import com.ilioadriano.transactions.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDomainServiceTest {

    @InjectMocks
    private TransactionDomainService transactionDomainService;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void shouldCreateNewTransactionInstance() {
        Account account = new Account();
        OperationType operationType = new OperationType();
        operationType.setType(OperationTypeEnum.CREDIT);

        Transaction transaction = transactionDomainService.createTransaction(account, operationType, BigDecimal.TEN);

        assertThat(transaction).isNotNull();
        assertThat(transaction.getAccount()).isEqualTo(account);
        assertThat(transaction.getOperationType()).isEqualTo(operationType);
        assertThat(transaction.getAmount()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shoudlSaveTransaction() {
        Transaction transaction = new Transaction();
        Transaction mockTransaction = new Transaction();

        when(transactionRepository.saveAndFlush(any())).thenReturn(mockTransaction);

        Transaction savedTransaction = transactionDomainService.save(transaction);

        verify(transactionRepository).saveAndFlush(eq(transaction));
        assertThat(savedTransaction).isEqualTo(mockTransaction);
    }

}