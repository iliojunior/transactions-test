package com.ilioadriano.transactions.rest;

import com.ilioadriano.transactions.application.service.TransactionApplicationService;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionCreationDTO;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller to handle /transaction requests
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionApplicationService transactionApplicationService;

    @PostMapping(value = "/transactions", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionCreationDTO transactionCreationDTO) {
        log.info("Received request to create transaction");
        return transactionApplicationService.create(transactionCreationDTO);
    }

}
