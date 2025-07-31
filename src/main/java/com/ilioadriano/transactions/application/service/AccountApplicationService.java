package com.ilioadriano.transactions.application.service;

import com.ilioadriano.transactions.application.mapper.AccountMapper;
import com.ilioadriano.transactions.domain.exception.DuplicateEntryException;
import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.domain.service.AccountDomainService;
import com.ilioadriano.transactions.rest.dto.account.AccountCreationDTO;
import com.ilioadriano.transactions.rest.dto.account.AccountResponseDTO;
import com.ilioadriano.transactions.rest.exception.UnprocessableEntityRestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.ilioadriano.transactions.rest.dto.ErrorResponseDTO.ErrorDTO;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Application service layer to {@link Account}
 * It is where connect rest layer with domain service layer
 * Here entity is created based on creation dto, map entity to response dto and create {@link ResponseEntity}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountApplicationService {

    private final AccountDomainService accountDomainService;
    private final AccountMapper accountMapper;

    public ResponseEntity<AccountResponseDTO> create(AccountCreationDTO accountDTO) {
        log.info("Creating account");
        log.debug("AccountCreationDTO: {}", accountDTO);

        Account account = accountDomainService.create(accountDTO.getDocumentNumber());

        try {
            accountDomainService.save(account);
        } catch (DuplicateEntryException e) {
            ErrorDTO errorDTO = ErrorDTO.builder()
                    .cause("Duplicate entry")
                    .message("Already exists an Account with this document number")
                    .build();
            throw UnprocessableEntityRestException.from(errorDTO);
        }

        log.info("Created account id {}", account.getId());

        AccountResponseDTO accountResponseDTO = accountMapper.accountToResponseDTO(account);
        return ResponseEntity.status(CREATED).body(accountResponseDTO);
    }

    public ResponseEntity<AccountResponseDTO> getAccountById(Integer accountId) {
        try {
            log.info("Getting account by id {}", accountId);

            Account account = accountDomainService.findById(accountId);
            AccountResponseDTO accountResponseDTO = accountMapper.accountToResponseDTO(account);

            log.debug("Found account {}", account);

            return ResponseEntity.ok(accountResponseDTO);
        } catch (EntityNotFoundException e) {
            log.error("Entity not found", e);
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }
}
