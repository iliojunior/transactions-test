package com.ilioadriano.transactions.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic model to response errors to client
 */
@Data
public class ErrorResponseDTO {

    private List<ErrorDTO> errors = new ArrayList<>();

    @Data
    @Builder
    public static class ErrorDTO {

        private String cause;
        private String message;

    }

}
