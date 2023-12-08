package com.nashss.se.GlobalGarage.activity.request;

/**
 * This class represents a request to delete a single item.
 * It is used as part of the DeleteItemActivity API.
 */
public class DeleteItemRequest {
    private final String itemId;
    private final String garageId;
    private final String sellerId;
    /**
     * Constructs a new DeleteItemRequest with the specified item ID and garage ID.
     * This constructor is private and is intended to be used by the Builder class for creating an instance.
     *
     * @param itemId   The ID of the item to be deleted.
     * @param garageId The ID of the garage to which the item belongs.
     * @param sellerId The ID of the seller to which the item belongs.
     */
    public DeleteItemRequest(String itemId, String garageId, String sellerId) {
        this.itemId = itemId;
        this.garageId = garageId;
        this.sellerId = sellerId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getGarageId() {
        return garageId;
    }

    public String getSellerId() {
        return sellerId;
    }


    @Override
    public String toString() {
        return "DeleteItemRequest{" +
                "itemId='" + itemId + '\'' +
                ", garageId='" + garageId + '\'' +
                ", sellerId='" + sellerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String itemId;
        private String garageId;
        private String sellerId;

        public Builder withItemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder withGarageId(String garageId) {
            this.garageId = garageId;
            return this;
        }
        public Builder withSellerId(String sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public DeleteItemRequest build() {
            return new DeleteItemRequest(itemId, garageId,sellerId);
        }
    }
}
