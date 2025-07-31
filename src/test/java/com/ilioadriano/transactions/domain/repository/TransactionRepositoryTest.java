package com.ilioadriano.transactions.domain.repository;

import com.ilioadriano.transactions.TestUtils;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.model.OperationType;
import com.ilioadriano.transactions.domain.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({"mysql-test", "test"})
class TransactionRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    /**
     * This considers operation type records already saved previously via Liquibase.
     */
    @Test
    void shouldInsertATransaction() {
        Account account = new Account();
        account.setDocumentNumber(TestUtils.generateRandomDocumentNumber());
        accountRepository.saveAndFlush(account);

        OperationType operationType = operationTypeRepository.findById(1).orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setAmount(BigDecimal.TEN.negate());
        Transaction savedtransaction = transactionRepository.saveAndFlush(transaction);

        assertThat(savedtransaction.getId()).isNotNull();
    }

}