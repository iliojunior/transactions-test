package com.ilioadriano.transactions.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents the transaction operation type
 */
@Data
@Entity
@Table(name = "OPERATION_TYPES")
public class OperationType {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private OperationTypeEnum type;

}
