package com.nashss.se.GlobalGarage.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents a request to express interest in an item.
 * This request includes the IDs of the buyer, the item, and the garage
 * where the item is listed.
 */
@JsonDeserialize(builder = ExpressInterestRequest.Builder.class)
public class ExpressInterestRequest {
    private final String buyerID;
    private final String itemID;
    private final String garageID;

    /**
     * Constructs a new ExpressInterestRequest.
     * @param buyerID The unique identifier of the buyer.
     * @param itemID The unique identifier of the item.
     * @param garageID The unique identifier of the garage where the item is listed.
     */

    public ExpressInterestRequest(String buyerID, String itemID, String garageID) {
        this.buyerID = buyerID;
        this.itemID = itemID;
        this.garageID = garageID;

    }

    // Getters
    public String getBuyerID() {
        return buyerID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getGarageID() {
        return garageID;
    }

    //CHECKSTYLE:OFF:Builder

    /**
     * Builder for ExpressInterestRequest.
     */
    public static class Builder {
        private String buyerID;
        private String itemID;
        private String garageID;

        public Builder withBuyerID(String buyerID) {
            this.buyerID = buyerID;
            return this;
        }

        public Builder withItemID(String itemID) {
            this.itemID = itemID;
            return this;
        }

        public Builder withGarageID(String garageID) {
            this.garageID = garageID;
            return this;
        }

        public ExpressInterestRequest build() {
            return new ExpressInterestRequest(buyerID, itemID, garageID);
        }
    }

    /**
     * Creates builder to build {@link ExpressInterestRequest}.
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }
}
