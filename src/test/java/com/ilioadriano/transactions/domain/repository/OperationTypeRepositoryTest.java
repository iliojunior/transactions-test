package com.ilioadriano.transactions.domain.repository;

import com.ilioadriano.transactions.domain.model.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.ilioadriano.transactions.domain.model.OperationTypeEnum.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({"mysql-test", "test"})
class OperationTypeRepositoryTest {

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    /**
     * This considers records already saved previously via Liquibase.
     */
    @Test
    void shouldReturnAnOperationType() {
        Optional<OperationType> operationType = operationTypeRepository.findById(1);

        assertThat(operationType).isNotEmpty();
        assertThat(operationType.get().getDescription()).isEqualTo("Normal Purchase");
        assertThat(operationType.get().getType()).isEqualTo(DEBIT);
    }

}