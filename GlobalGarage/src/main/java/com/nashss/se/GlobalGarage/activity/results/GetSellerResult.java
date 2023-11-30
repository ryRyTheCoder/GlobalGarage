package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.SellerModel;

/**
 * This class represents the result of a request to get a single seller.
 */
public class GetSellerResult {
    private final boolean success;
    private final String message;
    private final SellerModel sellerModel;

    private GetSellerResult(boolean success, String message, SellerModel sellerModel) {
        this.success = success;
        this.message = message;
        this.sellerModel = sellerModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public SellerModel getSellerModel() {
        return sellerModel;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private SellerModel sellerModel;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withSellerModel(SellerModel sellerModel) {
            this.sellerModel = sellerModel;
            return this;
        }

        public GetSellerResult build() {
            return new GetSellerResult(success, message, sellerModel);
        }
    }
}
