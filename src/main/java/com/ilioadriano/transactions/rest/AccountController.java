package com.ilioadriano.transactions.rest;

import com.ilioadriano.transactions.application.service.AccountApplicationService;
import com.ilioadriano.transactions.rest.dto.account.AccountCreationDTO;
import com.ilioadriano.transactions.rest.dto.account.AccountResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller to handle /accounts requests
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountApplicationService accountApplicationService;

    @PostMapping(value = "/accounts", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountCreationDTO accountDTO) {
        log.info("Received request to create account");
        return accountApplicationService.create(accountDTO);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable("id") Integer accountId) {
        log.info("Received request to get account by id {}", accountId);
        return accountApplicationService.getAccountById(accountId);
    }

}
