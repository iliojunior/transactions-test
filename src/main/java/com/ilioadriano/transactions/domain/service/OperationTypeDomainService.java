package com.ilioadriano.transactions.domain.service;

import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.OperationType;
import com.ilioadriano.transactions.domain.repository.OperationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Domain service layer to {@link OperationType}
 * It connect application service layer with repository layer
 * Here database exceptions are translated to application exceptions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperationTypeDomainService {

    private final OperationTypeRepository operationTypeRepository;

    public OperationType findById(Integer operationId) throws EntityNotFoundException {
        log.debug("Searching for operation type by id {}", operationId);

        return operationTypeRepository.findById(operationId)
                .orElseThrow(() -> EntityNotFoundException.fromId(operationId, OperationType.class));
    }

}
