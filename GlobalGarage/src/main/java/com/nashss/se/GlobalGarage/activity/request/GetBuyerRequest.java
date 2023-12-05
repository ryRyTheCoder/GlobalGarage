package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get a single buyer.
 * It is used as part of the GetBuyerActivity API.
 */
public class GetBuyerRequest {
    private final String buyerId;

    /**
     * Constructs a new GetBuyerRequest with the specified buyer ID.
     * This constructor is private and is intended to be used by the Builder class for creating an instance.
     *
     * @param buyerId The ID of the buyer to be retrieved.
     */
    public GetBuyerRequest(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    @Override
    public String toString() {
        return "GetBuyerRequest{" +
                "buyerId='" + buyerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String buyerId;

        public Builder withBuyerId(String buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        public GetBuyerRequest build() {
            return new GetBuyerRequest(buyerId);
        }
    }
}
