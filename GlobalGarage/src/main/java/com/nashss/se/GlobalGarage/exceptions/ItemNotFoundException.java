package com.nashss.se.GlobalGarage.exceptions;

/**
 * Exception to throw when no item found in the database.
 */
public class ItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3833648211684521672L;
    /**
     * Exception with no message or cause.
     */
    public ItemNotFoundException() {
    }
    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public ItemNotFoundException(Throwable cause) {
        super(cause);
    }
}
