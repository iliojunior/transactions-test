package com.ilioadriano.transactions.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Represents the transaction stored in database
 */
@Data
@Entity
@Table(name = "TRANSACTIONS")
@Immutable
public class Transaction {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPERATION_TYPE_ID")
    private OperationType operationType;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "EVENT_DATE")
    private OffsetDateTime eventDate;

}
