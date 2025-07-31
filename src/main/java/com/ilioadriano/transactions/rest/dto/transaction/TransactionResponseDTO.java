package com.ilioadriano.transactions.rest.dto.transaction;

import com.ilioadriano.transactions.rest.dto.account.AccountResponseDTO;
import com.ilioadriano.transactions.rest.dto.operationtype.OperationTypeResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Represents the model to response transaction data
 */
@Data
public class TransactionResponseDTO {

    private Integer id;
    private AccountResponseDTO account;
    private OperationTypeResponseDTO operationType;
    private BigDecimal amount;
    private OffsetDateTime eventDate;

}
