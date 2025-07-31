package com.ilioadriano.transactions.rest.exception;

import com.ilioadriano.transactions.rest.dto.ErrorResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Super class to represent rest exceptions with generic error model
 */
@Getter
@AllArgsConstructor
public class RestException extends RuntimeException {

    /**
     * Generic model to response errors to client
     */
    private ErrorResponseDTO errorResponseDTO;

}
