package com.nashss.se.GlobalGarage.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.util.Set;

/**
 * This class represents a request to create an item.
 * It is used as a part of the CreateItemActivity API.
 */
@JsonDeserialize(builder = CreateItemRequest.Builder.class)
public class CreateItemRequest {
    private final String sellerID;
    private final String garageID;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final String category;
    private final Set<String> images;

    /**
     * Constructs a new CreateItemRequest with the specified details.
     * This constructor is private and initializes a request to create an item with its
     * name, description, price, category, and images.
     * It is intended to be used internally by the class, typically through a Builder pattern.
     *
     * @param sellerID    The ID of the seller who is creating the item.
     * @param garageID    The ID of the garage where the item is located.
     * @param name        The name of the item.
     * @param description A description of the item.
     * @param price       The price of the item.
     * @param category    The category to which the item belongs.
     * @param images      A set of image URLs associated with the item.
     */

    public CreateItemRequest(String sellerID, String garageID, String name, String description,
                             BigDecimal price, String category, Set<String> images) {
        this.sellerID = sellerID;
        this.garageID = garageID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
    }

    // Getters
    public String getSellerID() {
        return sellerID;
    }

    public String getGarageID() {
        return garageID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public Set<String> getImages() {
        return images;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sellerID;
        private String garageID;
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
        private Set<String> images;

        public Builder withSellerID(String sellerID) {
            this.sellerID = sellerID;
            return this;
        }

        public Builder withGarageID(String garageID) {
            this.garageID = garageID;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withImages(Set<String> images) {
            this.images = images;
            return this;
        }

        public CreateItemRequest build() {
            return new CreateItemRequest(sellerID, garageID, name, description, price, category, images);
        }
    }
}
