package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.ItemModel;

import java.util.List;

import java.util.Objects;

/**
 * Result class for GetRecentItemsActivity.
 * Contains the outcome of fetching recent items.
 */
public class GetRecentItemsResult {
    private final boolean success;
    private final String message;
    private final List<ItemModel> itemModels;
    private final String lastEvaluatedKey;

    /**
     * Constructs a new GetRecentItemsResult object.
     *
     * @param success Indicates the success status of the operation.
     * @param message A message providing more details about the operation.
     * @param itemModels A list of ItemModel objects representing the items.
     * @param lastEvaluatedKey A string representing the last evaluated key, useful for pagination.
     */

    public GetRecentItemsResult(boolean success, String message,
                                List<ItemModel> itemModels, String lastEvaluatedKey) {
        this.success = success;
        this.message = message;
        this.itemModels = itemModels;
        this.lastEvaluatedKey = lastEvaluatedKey;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<ItemModel> getItemModels() {
        return itemModels;
    }

    public String getLastEvaluatedKey() {
        return lastEvaluatedKey;
    }

    @Override
    public String toString() {
        return "GetRecentItemsResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", itemModels=" + itemModels +
                ", lastEvaluatedKey='" + lastEvaluatedKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetRecentItemsResult that = (GetRecentItemsResult) o;
        return success == that.success && Objects.equals(message, that.message) &&
                Objects.equals(itemModels, that.itemModels) &&
                Objects.equals(lastEvaluatedKey, that.lastEvaluatedKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, itemModels, lastEvaluatedKey);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private List<ItemModel> itemModels;
        private String lastEvaluatedKey;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withItemModels(List<ItemModel> itemModels) {
            this.itemModels = itemModels;
            return this;
        }

        public Builder withLastEvaluatedKey(String lastEvaluatedKey) {
            this.lastEvaluatedKey = lastEvaluatedKey;
            return this;
        }

        public GetRecentItemsResult build() {
            return new GetRecentItemsResult(success, message, itemModels, lastEvaluatedKey);
        }
    }
}
