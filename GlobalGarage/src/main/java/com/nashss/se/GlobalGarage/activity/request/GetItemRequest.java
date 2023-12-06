package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get a single item.
 * It is used as part of the GetItemActivity API.
 */
public class GetItemRequest {
    private final String itemId;

    /**
     * Constructs a new GetItemRequest with the specified item ID.
     * This constructor is private and is intended to be used by the Builder class for creating an instance.
     *
     * @param itemId The ID of the item to be retrieved.
     */
    private GetItemRequest(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    @Override
    public String toString() {
        return "GetItemRequest{" +
                "itemId='" + itemId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String itemId;

        public Builder withItemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public GetItemRequest build() {
            return new GetItemRequest(itemId);
        }
    }
}
