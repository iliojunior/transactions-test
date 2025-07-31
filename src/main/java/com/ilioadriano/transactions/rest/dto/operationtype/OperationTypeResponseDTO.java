package com.ilioadriano.transactions.rest.dto.operationtype;

import com.ilioadriano.transactions.domain.model.OperationTypeEnum;
import lombok.Data;

/**
 * Represents the model to response operation type
 */
@Data
public class OperationTypeResponseDTO {

    private Integer id;
    private String description;
    private OperationTypeEnum type;

}
