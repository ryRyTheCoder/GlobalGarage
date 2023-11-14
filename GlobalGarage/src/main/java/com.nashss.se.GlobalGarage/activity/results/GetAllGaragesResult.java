package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.GarageModel;
import java.util.List;
import java.util.Map;

/**
 * This class represents the result of a request to get all garages.
 */
public class GetAllGaragesResult {
    private final boolean success;
    private final String message;
    private final List<GarageModel> garageModels;
    private final String lastEvaluatedKey;

    private GetAllGaragesResult(boolean success, String message, List<GarageModel> garageModels, String lastEvaluatedKey) {
        this.success = success;
        this.message = message;
        this.garageModels = garageModels;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<GarageModel> getGarageModels() {
        return garageModels;
    }

    public String getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }

    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private List<GarageModel> garageModels;
        private String lastEvaluatedKey;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withGarageModels(List<GarageModel> garageModels) {
            this.garageModels = garageModels;
            return this;
        }

        public Builder withLastEvaluatedKey(String lastEvaluatedKey) {
            this.lastEvaluatedKey = lastEvaluatedKey;
            return this;
        }

        public GetAllGaragesResult build() {
            return new GetAllGaragesResult(success, message, garageModels, lastEvaluatedKey);
        }
    }
}
