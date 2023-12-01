package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get all garages.
 * It is used as part of the GetAllGaragesActivity API.
 */
public class GetAllGaragesRequest {
    private final String lastEvaluatedKey;

    /**
     * Constructs a new GetAllGaragesRequest with a specified key for pagination.
     * This constructor is private and initializes a request with the last evaluated key,
     * primarily for pagination purposes.
     *
     * @param lastEvaluatedKey The last evaluated key used for fetching the next set of results in a paginated query.
     */

    public GetAllGaragesRequest(String lastEvaluatedKey) {
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    /**
     * Retrieves the last evaluated key used in the paginated query.
     * This key is used to fetch the next set of results from where the previous query ended.
     *
     * @return The last evaluated key for pagination.
     */

    public String getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }

    @Override
    public String toString() {
        return "GetAllGaragesRequest{" +
                "lastEvaluatedKey=" + lastEvaluatedKey +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String lastEvaluatedKey;

        public Builder withLastEvaluatedKey(String lastEvaluatedKey) {
            this.lastEvaluatedKey = lastEvaluatedKey;
            return this;
        }

        public GetAllGaragesRequest build() {
            return new GetAllGaragesRequest(lastEvaluatedKey);
        }
    }
}
