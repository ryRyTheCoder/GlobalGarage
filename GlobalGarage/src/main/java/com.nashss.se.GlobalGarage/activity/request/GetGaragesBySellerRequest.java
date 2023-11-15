package com.nashss.se.GlobalGarage.activity.request;

import java.util.Objects;

public class GetGaragesBySellerRequest {
    private String sellerId;
    private Integer limit; // Optional, for pagination
    private String lastEvaluatedKey; // Optional, for pagination

    public GetGaragesBySellerRequest() {
        // Default constructor
    }

    // Constructor with all fields
    public GetGaragesBySellerRequest(String sellerId, Integer limit, String lastEvaluatedKey) {
        this.sellerId = sellerId;
        this.limit = limit;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    // Getters
    public String getSellerId() {
        return sellerId;
    }


    public Integer getLimit() {
        return limit;
    }


    public String getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }


    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sellerId;
        private Integer limit;
        private String lastEvaluatedKey;

        public Builder withSellerId(String sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder withLastEvaluatedKey(String lastEvaluatedKey) {
            this.lastEvaluatedKey = lastEvaluatedKey;
            return this;
        }

        public GetGaragesBySellerRequest build() {
            return new GetGaragesBySellerRequest(sellerId, limit, lastEvaluatedKey);
        }
    }

    // Overridden equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetGaragesBySellerRequest that = (GetGaragesBySellerRequest) o;
        return Objects.equals(sellerId, that.sellerId) && Objects.equals(limit, that.limit) && Objects.equals(lastEvaluatedKey, that.lastEvaluatedKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerId, limit, lastEvaluatedKey);
    }

    @Override
    public String toString() {
        return "GetGaragesBySellerRequest{" +
                "sellerId='" + sellerId + '\'' +
                ", limit=" + limit +
                ", lastEvaluatedKey='" + lastEvaluatedKey + '\'' +
                '}';
    }
}