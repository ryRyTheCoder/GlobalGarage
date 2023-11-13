package com.nashss.se.GlobalGarage.models;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ItemModel {
    private final String garageID;
    private final String itemID;
    private final String sellerID;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final String category;
    private final Set<String> images;
    private final String dateListed;
    private final Set<String> buyersInterested;
    private final String status;

    private ItemModel(String garageID, String itemID, String sellerID, String name, String description,
                      BigDecimal price, String category, Set<String> images, String dateListed,
                      Set<String> buyersInterested, String status) {
        this.garageID = garageID;
        this.itemID = itemID;
        this.sellerID = sellerID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
        this.dateListed = dateListed;
        this.buyersInterested = buyersInterested;
        this.status = status;
    }

    // Getters

    public String getGarageID() {
        return garageID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getSellerID() {
        return sellerID;
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

    public String getDateListed() {
        return dateListed;
    }

    public Set<String> getBuyersInterested() {
        return buyersInterested;
    }

    public String getStatus() {
        return status;
    }

    // equals() and hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemModel that = (ItemModel) o;
        return Objects.equals(garageID, that.garageID) &&
                Objects.equals(itemID, that.itemID) &&
                Objects.equals(sellerID, that.sellerID) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(category, that.category) &&
                Objects.equals(images, that.images) &&
                Objects.equals(dateListed, that.dateListed) &&
                Objects.equals(buyersInterested, that.buyersInterested) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(garageID, itemID, sellerID, name, description, price, category, images, dateListed, buyersInterested, status);
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String garageID;
        private String itemID;
        private String sellerID;
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
        private Set<String> images;
        private String dateListed;
        private Set<String> buyersInterested;
        private String status;

        public Builder withGarageID(String garageID) {
            this.garageID = garageID;
            return this;
        }

        public Builder withItemID(String itemID) {
            this.itemID = itemID;
            return this;
        }

        public Builder withSellerID(String sellerID) {
            this.sellerID = sellerID;
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

        public Builder withDateListed(String dateListed) {
            this.dateListed = dateListed;
            return this;
        }

        public Builder withBuyersInterested(Set<String> buyersInterested) {
            this.buyersInterested = buyersInterested;
            return this;
        }

        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }


        public ItemModel build() {
            return new ItemModel(garageID, itemID, sellerID, name, description, price, category, images, dateListed, buyersInterested, status);
        }
    }
}