package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get all garages.
 * It is used as part of the GetAllGaragesActivity API.
 */
public class GetAllGaragesRequest {
    private final String lastEvaluatedKey;

    private GetAllGaragesRequest(String lastEvaluatedKey) {
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    public String getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }

    @Override
    public String toString() {
        return "GetAllGaragesRequest{" +
                "lastEvaluatedKey=" + lastEvaluatedKey +
                '}';
    }

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
