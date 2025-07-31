package com.ilioadriano.transactions.domain.service;

import com.ilioadriano.transactions.domain.exception.DuplicateEntryException;
import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Domain service layer to {@link Account}
 * It connect application service layer with repository layer
 * Here entity is initialized, database exceptions are translated to application exceptions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDomainService {

    private final AccountRepository accountRepository;

    public Account create(String documentNumber) {
        Account account = new Account();
        account.setDocumentNumber(documentNumber);
        return account;
    }

    public Account save(Account account) throws DuplicateEntryException {
        try {
            return accountRepository.saveAndFlush(account);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getMessage().contains("Duplicate entry")) {
                throw new DuplicateEntryException(exception);
            }
            throw exception;
        }
    }

    public Account findById(Integer accountId) throws EntityNotFoundException {
        log.info("Searching account by id: {}", accountId);

        return accountRepository.findById(accountId)
                .orElseThrow(() -> EntityNotFoundException.fromId(accountId, Account.class));
    }

}
