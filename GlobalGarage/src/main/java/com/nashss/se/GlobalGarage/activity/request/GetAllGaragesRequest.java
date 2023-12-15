package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get all garages.
 * It is used as part of the GetAllGaragesActivity API.
 */
public class GetAllGaragesRequest {
    private final String lastEvaluatedKey;
    private final Integer limit;

    /**
     * Constructs a new GetAllGaragesRequest with a specified key for pagination.
     * This constructor is private and initializes a request with the last evaluated key,
     * primarily for pagination purposes.
     *
     * @param lastEvaluatedKey The last evaluated key used for fetching the next set of results in a paginated query.
     * @param limit            The desired limit for responses
     */

    public GetAllGaragesRequest(String lastEvaluatedKey, Integer limit) {
        this.lastEvaluatedKey = lastEvaluatedKey;
        this.limit = limit;
    }

    //Getters
    /**
     * Retrieves the last evaluated key used in the paginated query.
     * This key is used to fetch the next set of results from where the previous query ended.
     *
     * @return The last evaluated key for pagination.
     */

    public String getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }

    public Integer getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "GetAllGaragesRequest{" +
                "lastEvaluatedKey=" + lastEvaluatedKey +
                ", limit=" + limit +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String lastEvaluatedKey;
        private Integer limit;

        public Builder withLastEvaluatedKey(String lastEvaluatedKey) {
            this.lastEvaluatedKey = lastEvaluatedKey;
            return this;
        }

        public Builder withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public GetAllGaragesRequest build() {
            return new GetAllGaragesRequest(lastEvaluatedKey, limit);
        }
    }
}
