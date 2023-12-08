package com.nashss.se.GlobalGarage.activity.results;

/**
 * Represents the result of a delete item operation.
 */
public class DeleteItemResult {
    private final boolean success;
    private final String message;

    /**
     * Private constructor for DeleteItemResult.
     *
     * @param success Indicates whether the delete operation was successful.
     * @param message A message associated with the operation's outcome.
     */
    private DeleteItemResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DeleteItemResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder

    /**
     * Creates a new Builder for DeleteItemResult.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for DeleteItemResult.
     */
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

        public DeleteItemResult build() {
            return new DeleteItemResult(success, message);
        }
    }
}
