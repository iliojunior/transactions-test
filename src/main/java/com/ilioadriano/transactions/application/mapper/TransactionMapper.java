package com.ilioadriano.transactions.application.mapper;

import com.ilioadriano.transactions.domain.model.Transaction;
import com.ilioadriano.transactions.rest.dto.transaction.TransactionResponseDTO;
import org.mapstruct.Mapper;

/**
 * Provide methods to map {@link TransactionResponseDTO} and {@link Transaction}
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponseDTO transactionToTransactionResponse(Transaction transaction);

}
