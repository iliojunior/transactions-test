package com.ilioadriano.transactions.domain.model;

import java.math.BigDecimal;


/**
 * Represents the type of financial operation, either a credit or a debit.
 * Each operation type defines how a given amount should be prepared
 */
public enum OperationTypeEnum {
    /**
     * Represents a credit operation.
     * When preparing a credit amount, the original positive value is retained.
     */
    CREDIT {
        @Override
        public BigDecimal prepare(BigDecimal amount) {
            return amount;
        }
    },
    /**
     * Represents a debit operation.
     * When preparing a debit amount, the original positive value is negated.
     */
    DEBIT {
        @Override
        public BigDecimal prepare(BigDecimal amount) {
            return amount.negate();
        }
    };

    /**
     * Abstract method to prepare a given amount based on the specific operation type.
     * Implementations for {@code CREDIT} and {@code DEBIT} define how the amount is adjusted.
     *
     * @param amount The original amount to be prepared.
     * @return The prepared {@link BigDecimal} amount, adjusted according to the operation type.
     */
    public abstract BigDecimal prepare(BigDecimal amount);
}
