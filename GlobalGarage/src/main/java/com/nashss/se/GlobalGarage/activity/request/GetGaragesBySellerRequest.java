package com.nashss.se.GlobalGarage.activity.request;

import java.util.Objects;

public class GetGaragesBySellerRequest {
    private String sellerId;
    private Integer limit;
    private String lastEvaluatedKey;

    /**
     * No-argument default constructor for GetGaragesBySellerRequest.
     * Initializes an empty request instance. This is useful when the request is
     * intended to be populated post-construction.
     */

    public GetGaragesBySellerRequest() {
        // Default constructor
    }

    /**
     * Constructs a new GetGaragesBySellerRequest with specified details, including pagination parameters.
     * Initializes a request to fetch garages by a specific seller, with optional limit and pagination key.
     *
     * @param sellerId         The ID of the seller whose garages
     *                         are to be fetched.
     * @param limit            Optional parameter to limit the number of
     *                         garages returned in a single request (for pagination).
     * @param lastEvaluatedKey Optional parameter representing the last evaluated key in a paginated
     *                         query (for pagination).
     */

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


    //CHECKSTYLE:OFF:Builder
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
        return Objects.equals(sellerId, that.sellerId) && Objects.equals(limit, that.limit) &&
                Objects.equals(lastEvaluatedKey, that.lastEvaluatedKey);
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
