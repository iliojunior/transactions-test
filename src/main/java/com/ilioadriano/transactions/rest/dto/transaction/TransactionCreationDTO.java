package com.ilioadriano.transactions.rest.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents the model to create transaction
 */
@Data
public class TransactionCreationDTO {

    @NotNull
    private Integer accountId;

    @NotNull
    private Integer operationTypeId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

}
