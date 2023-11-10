package com.nashss.se.GlobalGarage.exceptions;

/**
 * Exception to throw when no buyer is found in the database.
 */
public class BuyerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3833648211684521672L;
    /**
     * Exception with no message or cause.
     */
    public BuyerNotFoundException() {
    }
    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public BuyerNotFoundException(String message) {
        super(message);
    }
    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public BuyerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public BuyerNotFoundException(Throwable cause) {
        super(cause);
    }
}


