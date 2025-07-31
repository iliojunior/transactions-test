package com.ilioadriano.transactions.application.mapper;

import com.ilioadriano.transactions.domain.model.Account;
import com.ilioadriano.transactions.rest.dto.account.AccountResponseDTO;
import org.mapstruct.Mapper;

/**
 * Provide methods to map {@link AccountResponseDTO} and {@link Account}
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDTO accountToResponseDTO(Account account);

}
