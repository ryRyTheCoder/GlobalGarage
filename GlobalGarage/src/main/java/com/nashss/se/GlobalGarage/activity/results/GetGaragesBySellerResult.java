package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.GarageModel;

import java.util.List;
import java.util.Objects;

public class GetGaragesBySellerResult {
    private final boolean success;
    private final String message;
    private final List<GarageModel> garageModels;
    private final String lastEvaluatedKey;

    private GetGaragesBySellerResult(boolean success, String message,
                                     List<GarageModel> garageModels, String lastEvaluatedKey) {
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

    //CHECKSTYLE:OFF:Builder
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

        public GetGaragesBySellerResult build() {
            return new GetGaragesBySellerResult(success, message, garageModels, lastEvaluatedKey);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetGaragesBySellerResult that = (GetGaragesBySellerResult) o;
        return success == that.success &&
                Objects.equals(message, that.message) &&
                Objects.equals(garageModels, that.garageModels) &&
                Objects.equals(lastEvaluatedKey, that.lastEvaluatedKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, garageModels, lastEvaluatedKey);
    }

    @Override
    public String toString() {
        return "GetGaragesBySellerResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", garages=" + garageModels +
                ", lastEvaluatedKey='" + lastEvaluatedKey + '\'' +
                '}';
    }
}
