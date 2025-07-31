package com.ilioadriano.transactions.domain.exception;

/**
 * Exception to represents when try to insert an entity and data integrity is violated
 */
public class DuplicateEntryException extends Throwable {

    public DuplicateEntryException() {
    }

    public DuplicateEntryException(Throwable cause) {
        super(cause);
    }

}
