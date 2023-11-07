package com.nashss.se.GlobalGarage.activity.results;

/**
 * This class represents the result of a request to create a seller.
 */
public class CreateSellerResult {
    private final boolean success;
    private final String message;
    private final String sellerID;

    private CreateSellerResult(boolean success, String message, String sellerID) {
        this.success = success;
        this.message = message;
        this.sellerID = sellerID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getSellerID() {
        return sellerID;
    }

    @Override
    public String toString() {
        return "CreateSellerResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", sellerID='" + sellerID + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private String sellerID;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withSellerID(String sellerID) {
            this.sellerID = sellerID;
            return this;
        }

        public CreateSellerResult build() {
            return new CreateSellerResult(success, message, sellerID);
        }
    }
}
