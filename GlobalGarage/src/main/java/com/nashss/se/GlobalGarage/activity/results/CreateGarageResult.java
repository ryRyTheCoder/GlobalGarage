package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.GarageModel;

/**
 * This class represents the result of a request to create a garage.
 */
public class CreateGarageResult {
    private final boolean success;
    private final String message;
    private final GarageModel garageModel;

    private CreateGarageResult(boolean success, String message, GarageModel garageModel) {
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

    @Override
    public String toString() {
        return "CreateGarageResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", garageModel=" + garageModel +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
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

        public CreateGarageResult build() {
            return
                    new CreateGarageResult(success, message, garageModel);
        }
    }
}
