package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.BuyerModel;

/**
 * This class represents the result of a request to create a buyer.
 */
public class CreateBuyerResult {
    private final boolean success;
    private final String message;
    private final BuyerModel buyerModel;

    private CreateBuyerResult(boolean success, String message, BuyerModel buyerModel) {
        this.success = success;
        this.message = message;
        this.buyerModel = buyerModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BuyerModel getBuyerModel() {
        return buyerModel;
    }

    @Override
    public String toString() {
        return "CreateBuyerResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", buyerModel='" + buyerModel + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private BuyerModel buyerModel;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withBuyerModel(BuyerModel buyerModel) {
            this.buyerModel = buyerModel;
            return this;
        }

        public CreateBuyerResult build() {
            return
                    new CreateBuyerResult(success, message, buyerModel);
        }
    }
}
