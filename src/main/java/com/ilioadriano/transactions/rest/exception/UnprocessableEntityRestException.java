package com.ilioadriano.transactions.rest.exception;

import com.ilioadriano.transactions.rest.advice.GlobalExceptionHandler;
import com.ilioadriano.transactions.rest.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Exception thrown when wants to response {@link HttpStatus#UNPROCESSABLE_ENTITY}
 * Catch on {@link GlobalExceptionHandler} to build {@link ResponseEntity}
 */
public class UnprocessableEntityRestException extends RestException {

    public UnprocessableEntityRestException(ErrorResponseDTO errorResponseDTO) {
        super(errorResponseDTO);
    }

    public static UnprocessableEntityRestException from(ErrorResponseDTO.ErrorDTO errorDTO) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setErrors(List.of(errorDTO));

        return new UnprocessableEntityRestException(errorResponseDTO);
    }

}
