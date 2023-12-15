package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.BuyerModel;

/**
 * Represents the result of an attempt to update a buyer's information.
 */
public class UpdateBuyerResult {
    private final boolean success;
    private final String message;
    private final BuyerModel buyer;

    /**
     * Constructs an UpdateBuyerResult.
     *
     * @param success Indicates whether the update was successful.
     * @param message Provides details about the update operation.
     * @param buyer The updated buyer model.
     */
    private UpdateBuyerResult(boolean success, String message, BuyerModel buyer) {
        this.success = success;
        this.message = message;
        this.buyer = buyer;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BuyerModel getBuyer() {
        return buyer;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private BuyerModel buyer;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withBuyer(BuyerModel buyer) {
            this.buyer = buyer;
            return this;
        }

        public UpdateBuyerResult build() {
            return new UpdateBuyerResult(success, message, buyer);
        }
    }
}
