package com.nashss.se.GlobalGarage.activity.request;

import java.util.Objects;

/**
 * Represents a request to retrieve a list of recent items, potentially with pagination.
 * This request class is used to interact with endpoints that provide data on recently listed items.
 */

public class GetRecentItemsRequest {
    private Integer limit;
    private String lastEvaluatedKey;

    /**
     * Constructs an empty GetRecentItemsRequest.
     * no-argument constructor
     */

    public GetRecentItemsRequest() {
    }

    /**
     * Constructs a GetRecentItemsRequest with specific limit and lastEvaluatedKey.
     * This constructor is used to create a request with pagination parameters.
     *
     * @param limit            The maximum number of items to return in the response.
     * @param lastEvaluatedKey The last evaluated key from a previous request, used for pagination.
     */

    public GetRecentItemsRequest(Integer limit, String lastEvaluatedKey) {
        this.limit = limit;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    /**
     * Constructs a GetRecentItemsRequest with default limit and lastEvaluatedKey.
     * This constructor is used to create a request with pagination parameters.
     *
     * @param lastEvaluatedKey The last evaluated key from a previous request, used for pagination.
     */

    public GetRecentItemsRequest(String lastEvaluatedKey) {
        this.limit = 5;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    // Getters
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
        private Integer limit;
        private String lastEvaluatedKey;

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder withLastEvaluatedKey(String lastEvaluatedKey) {
            this.lastEvaluatedKey = lastEvaluatedKey;
            return this;
        }

        public GetRecentItemsRequest build() {
            return new GetRecentItemsRequest(limit, lastEvaluatedKey);
        }
    }

    // Overridden equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetRecentItemsRequest that = (GetRecentItemsRequest) o;
        return Objects.equals(limit, that.limit) &&
                Objects.equals(lastEvaluatedKey, that.lastEvaluatedKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, lastEvaluatedKey);
    }

    @Override
    public String toString() {
        return "GetRecentItemsRequest{" +
                "limit=" + limit +
                ", lastEvaluatedKey='" + lastEvaluatedKey + '\'' +
                '}';
    }
}
