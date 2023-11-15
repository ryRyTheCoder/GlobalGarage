package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.GarageModel;

/**
 * This class represents the result of a request to get a single garage.
 */
public class GetOneGarageResult {
    private final boolean success;
    private final String message;
    private final GarageModel garageModel;

    private GetOneGarageResult(boolean success, String message, GarageModel garageModel) {
        this.success = success;
        this.message = message;
        this.garageModel = garageModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public GarageModel getGarageModel() {
        return garageModel;
    }

    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private GarageModel garageModel;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withGarageModel(GarageModel garageModel) {
            this.garageModel = garageModel;
            return this;
        }

        public GetOneGarageResult build() {
            return new GetOneGarageResult(success, message, garageModel);
        }
    }
}
