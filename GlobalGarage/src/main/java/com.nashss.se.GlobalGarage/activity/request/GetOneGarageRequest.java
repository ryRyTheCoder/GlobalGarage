package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get a single garage.
 * It is used as part of the GetOneGarageActivity API.
 */

public class GetOneGarageRequest {
    private final String sellerId;
    private final String garageId;

    private GetOneGarageRequest(String sellerId, String garageId) {
        this.sellerId = sellerId;
        this.garageId = garageId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getGarageId() {
        return garageId;
    }


    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "GetOneGarageRequest{" +
                "sellerId='" + sellerId + '\'' +
                ", garageId='" + garageId + '\'' +
                '}';
    }

    public static class Builder {
        private String sellerId;
        private String garageId;

        public Builder withGarageId(String garageId) {
            this.garageId = garageId;
            return this;
        }
        public Builder withSellerId(String sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public GetOneGarageRequest build() {
            return new GetOneGarageRequest(sellerId,garageId);
        }
    }
}