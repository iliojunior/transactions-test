package com.ilioadriano.transactions.rest.advice;

import com.ilioadriano.transactions.rest.dto.ErrorResponseDTO;
import com.ilioadriano.transactions.rest.exception.UnprocessableEntityRestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.ilioadriano.transactions.rest.dto.ErrorResponseDTO.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

/**
 * Global controller advice to handle application exceptions
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles unprocessable entity exception translating to human language
     *
     * @param unprocessableEntityRestException The UnprocessableEntityRestException thrown by application
     * @return A ResponseEntity containing the custom error DTO and a UNPROCESSABLE_ENTITY status.
     */
    @ExceptionHandler(UnprocessableEntityRestException.class)
    public ResponseEntity<ErrorResponseDTO> unprocessableEntity(UnprocessableEntityRestException unprocessableEntityRestException) {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(unprocessableEntityRestException.getErrorResponseDTO());
    }

    /**
     * Handles MethodArgumentNotValidException, which is thrown when a @Valid or @Validated
     * annotated method parameter fails validation. It collects all field errors and
     * structures them into a custom ValidationErrorResponse DTO.
     *
     * @param ex The MethodArgumentNotValidException thrown by Spring.
     * @return A ResponseEntity containing the custom error DTO and a BAD_REQUEST status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDTO> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ErrorDTO.builder()
                        .cause(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()
                )
                .collect(Collectors.toList());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setErrors(violations);

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}
