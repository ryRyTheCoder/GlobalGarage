package com.nashss.se.GlobalGarage.activity.results;

/**
 * Represents the result of an attempt to express interest in an item.
 * This result includes whether the operation was successful and a message describing the outcome.
 */
public class ExpressInterestResult {
    private final boolean success;
    private final String message;

    /**
     * Constructs an ExpressInterestResult.
     *
     * @param success Indicates whether the expression of interest was successful.
     * @param message Provides details about the outcome of the operation.
     */
    private ExpressInterestResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    //CHECKSTYLE:OFF:Builder

    public static class Builder {
        private boolean success;
        private String message;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ExpressInterestResult build() {
            return new ExpressInterestResult(success, message);
        }
    }

    /**
     * Creates builder to build {@link ExpressInterestResult}.
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }
}
