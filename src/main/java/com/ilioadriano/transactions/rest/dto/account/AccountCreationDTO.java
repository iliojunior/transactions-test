package com.ilioadriano.transactions.rest.dto.account;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents model to create account
 */
@Data
public class AccountCreationDTO {

    @NotNull
    @NotEmpty
    private String documentNumber;

}
