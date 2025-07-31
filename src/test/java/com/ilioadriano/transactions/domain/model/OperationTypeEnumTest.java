package com.ilioadriano.transactions.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.ilioadriano.transactions.domain.model.OperationTypeEnum.CREDIT;
import static com.ilioadriano.transactions.domain.model.OperationTypeEnum.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;

class OperationTypeEnumTest {

    @Test
    void shouldReturnParameter() {
        BigDecimal preparedValue = CREDIT.prepare(BigDecimal.TEN);
        assertThat(preparedValue).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldReturnNegativeParameter() {
        BigDecimal preparedValue = DEBIT.prepare(BigDecimal.TEN);
        assertThat(preparedValue).isEqualTo(BigDecimal.TEN.negate());
    }

}