package com.ilioadriano.transactions.domain.service;

import com.ilioadriano.transactions.domain.exception.EntityNotFoundException;
import com.ilioadriano.transactions.domain.model.OperationType;
import com.ilioadriano.transactions.domain.model.OperationTypeEnum;
import com.ilioadriano.transactions.domain.repository.OperationTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationTypeDomainServiceTest {

    @InjectMocks
    private OperationTypeDomainService operationTypeDomainService;

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @Test
    void shouldGetOperationTypeById() throws EntityNotFoundException {
        OperationType mockOperationType = new OperationType();
        mockOperationType.setId(1);
        mockOperationType.setDescription("Normal Purchase");
        mockOperationType.setType(OperationTypeEnum.DEBIT);

        when(operationTypeRepository.findById(eq(1))).thenReturn(Optional.of(mockOperationType));

        OperationType resultOperationType = operationTypeDomainService.findById(1);

        assertThat(resultOperationType).isNotNull();
        assertThat(resultOperationType).isEqualTo(mockOperationType);
    }

    @Test
    void shouldThrowIfOperationDoesNotExists() {
        when(operationTypeRepository.findById(eq(1))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operationTypeDomainService.findById(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("OperationType with id 1 was not found.");
    }

}