package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to get a single item.
 * It is used as part of the GetItemActivity API.
 */
public class GetItemRequest {
    private final String itemId;
    private final String garageId;

    /**
     * Constructs a new GetItemRequest with the specified item ID and garage ID.
     * This constructor is private and is intended to be used by the Builder class for creating an instance.
     *
     * @param itemId   The ID of the item to be retrieved.
     * @param garageId The ID of the garage to which the item belongs.
     */
    private GetItemRequest(String itemId, String garageId) {
        this.itemId = itemId;
        this.garageId = garageId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getGarageId() {
        return garageId;
    }

    @Override
    public String toString() {
        return "GetItemRequest{" +
                "itemId='" + itemId + '\'' +
                ", garageId='" + garageId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String itemId;
        private String garageId;

        public Builder withItemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder withGarageId(String garageId) {
            this.garageId = garageId;
            return this;
        }

        public GetItemRequest build() {
            return new GetItemRequest(itemId, garageId);
        }
    }
}

