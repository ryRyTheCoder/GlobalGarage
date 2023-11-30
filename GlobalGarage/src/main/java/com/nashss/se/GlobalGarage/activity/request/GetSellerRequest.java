package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get a single seller.
 * It is used as part of the GetSellerActivity API.
 */
public class GetSellerRequest {
    private final String sellerId;

    /**
     * Constructs a new GetSellerRequest with the specified seller ID.
     * This constructor is private and is intended to be used by the Builder class for creating an instance.
     *
     * @param sellerId The ID of the seller to be retrieved.
     */

    private GetSellerRequest(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return sellerId;
    }



    @Override
    public String toString() {
        return "GetSellerRequest{" +
                "sellerId='" + sellerId + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String sellerId;

        public Builder withSellerId(String sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public GetSellerRequest build() {
            return new GetSellerRequest(sellerId);
        }
    }
}
