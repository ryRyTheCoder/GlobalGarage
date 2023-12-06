package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.SellerModel;

/**
 * Represents the result of an attempt to update a seller's information.
 */
public class UpdateSellerResult {
    private final boolean success;
    private final String message;
    private final SellerModel seller;

    /**
     * Constructs an UpdateSellerResult.
     *
     * @param success Indicates whether the update was successful.
     * @param message Provides details about the update operation.
     * @param seller The updated seller model.
     */
    private UpdateSellerResult(boolean success, String message, SellerModel seller) {
        this.success = success;
        this.message = message;
        this.seller = seller;
    }
    //Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public SellerModel getSeller() {
        return seller;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private SellerModel seller;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withSeller(SellerModel seller) {
            this.seller = seller;
            return this;
        }

        public UpdateSellerResult build() {
            return new UpdateSellerResult(success, message, seller);
        }
    }
}
