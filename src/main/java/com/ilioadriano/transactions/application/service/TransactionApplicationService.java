package com.ilioadriano.transactions.application.service;

import com.ilioadriano.transactions.application.mapper.TransactionMapper;
import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.model.OperationType;
import com.ilioadriano.transactions.domain.model.Transaction;
import com.ilioadriano.transactions.domain.service.AccountDomainService;
import com.ilioadriano.transactions.domain.service.OperationTypeDomainService;
import com.ilioadriano.transactions.domain.service.TransactionDomainService;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionCreationDTO;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionResponseDTO;
import com.ilioadriano.transactions.rest.exception.UnprocessableEntityRestException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.ilioadriano.transactions.rest.dto.ErrorResponseDTO.ErrorDTO;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * Application service layer to {@link Transaction}
 * It is where connect rest layer with domain service layer
 * Here entity is created based on creation dto, map entity to response dto and create {@link ResponseEntity}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionApplicationService {

    private final TransactionMapper transactionMapper;
    private final TransactionDomainService transactionDomainService;

    private final AccountDomainService accountDomainService;
    private final OperationTypeDomainService operationTypeDomainService;

    @Transactional
    public ResponseEntity<TransactionResponseDTO> create(TransactionCreationDTO transactionCreationDTO) {
        log.info("Looking for related entities");
        OperationType operationType = findOperationType(transactionCreationDTO.getOperationTypeId());
        Account account = findAccountById(transactionCreationDTO.getAccountId());

        log.info("Creating transaction");
        Transaction transaction = transactionDomainService.createTransaction(account, operationType, transactionCreationDTO.getAmount());
        transactionDomainService.save(transaction);

        log.info("Transaction created");
        TransactionResponseDTO transactionResponseDTO = transactionMapper.transactionToTransactionResponse(transaction);
        return ResponseEntity.status(CREATED).body(transactionResponseDTO);
    }

    private OperationType findOperationType(@NotNull Integer operationTypeId) {
        try {
            return operationTypeDomainService.findById(operationTypeId);
        } catch (EntityNotFoundException e) {
            throw buildUnprocessableException(OperationType.class, operationTypeId);
        }
    }

    private Account findAccountById(@NotNull Integer accountId) {
        try {
            return accountDomainService.findById(accountId);
        } catch (EntityNotFoundException e) {
            throw buildUnprocessableException(Account.class, accountId);
        }
    }

    private UnprocessableEntityRestException buildUnprocessableException(Class<?> aClass, @NotNull Integer id) {
        String message = String.format("%s not found with id %s", aClass.getSimpleName(), id);
        ErrorDTO errorDTO = ErrorDTO.builder()
                .cause("Entity not found")
                .message(message)
                .build();

        return UnprocessableEntityRestException.from(errorDTO);
    }

}
