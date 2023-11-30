package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.ItemModel;

/**
 * This class represents the result of a request to create an item.
 */
public class CreateItemResult {
    private final boolean success;
    private final String message;
    private final ItemModel itemModel;

    private CreateItemResult(boolean success, String message, ItemModel itemModel) {
        this.success = success;
        this.message = message;
        this.itemModel = itemModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ItemModel getItemModel() {
        return itemModel;
    }

    @Override
    public String toString() {
        return "CreateItemResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", itemModel=" + itemModel +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private ItemModel itemModel;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withItemModel(ItemModel itemModel) {
            this.itemModel = itemModel;
            return this;
        }

        public CreateItemResult build() {
            return
                    new CreateItemResult(success, message, itemModel);
        }
    }
}
