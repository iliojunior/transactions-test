package com.ilioadriano.transactions.domain.exception;

/**
 * Exception to represents when try to find an entity returns not found
 */
public class EntityNotFoundException extends Throwable {

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException fromId(Object id, Class<?> entityClass) {
        String message = String.format("%s with id %s was not found.", entityClass.getSimpleName(), id);
        return new EntityNotFoundException(message);
    }

}
