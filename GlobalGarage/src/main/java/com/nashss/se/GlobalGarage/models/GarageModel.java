package com.nashss.se.GlobalGarage.models;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;


public class GarageModel {
    private final String sellerID;
    private final String garageID;
    private final String garageName;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String location;
    private final String description;
    private final List<String> items;
    private final Boolean isActive;

    private GarageModel(String sellerID, String garageID, String garageName, LocalDateTime startDate,
                        LocalDateTime endDate, String location, String description,
                        List<String> items, Boolean isActive) {
        this.sellerID = sellerID;
        this.garageID = garageID;
        this.garageName = garageName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
        this.items = items;
        this.isActive = isActive;
    }

    // Getters

    public String getSellerID() {
        return sellerID;
    }

    public String getGarageID() {
        return garageID;
    }

    public String getGarageName() {
        return garageName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getItems() {
        return items;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // equals() and hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GarageModel that = (GarageModel) o;
        return Objects.equals(sellerID, that.sellerID) &&
                Objects.equals(garageID, that.garageID) &&
                Objects.equals(garageName, that.garageName) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(location, that.location) &&
                Objects.equals(description, that.description) &&
                Objects.equals(items, that.items) &&
                Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerID, garageID, garageName, startDate, endDate, location, description, items, isActive);
    }

    //CHECKSTYLE:OFF:Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sellerID;
        private String garageID;
        private String garageName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String location;
        private String description;
        private List<String> items;
        private Boolean isActive;

        public Builder withSellerID(String sellerID) {
            this.sellerID = sellerID;
            return this;
        }

        public Builder withGarageID(String garageID) {
            this.garageID = garageID;
            return this;
        }

        public Builder withGarageName(String garageName) {
            this.garageName = garageName;
            return this;
        }

        public Builder withStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withItems(List<String> items) {
            this.items = items;
            return this;
        }

        public Builder withIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public GarageModel build() {
            return new GarageModel(sellerID, garageID, garageName, startDate,
                    endDate, location, description, items, isActive);
        }
    }
}
