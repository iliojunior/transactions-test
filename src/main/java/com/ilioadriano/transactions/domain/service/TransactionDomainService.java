package com.ilioadriano.transactions.domain.service;

import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.model.OperationType;
import com.ilioadriano.transactions.domain.model.Transaction;
import com.ilioadriano.transactions.domain.repository.TransactionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Domain service layer to {@link Transaction}
 * It connect application service layer with repository layer
 * Here entity is initialized, database exceptions are translated to application exceptions.
 */
@Service
@RequiredArgsConstructor
public class TransactionDomainService {

    private final TransactionRepository transactionRepository;

    public Transaction createTransaction(Account account, OperationType operationType, @NotNull BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setAmount(operationType.getType().prepare(amount));
        return transaction;
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.saveAndFlush(transaction);
    }

}
