package com.nashss.se.GlobalGarage.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class BuyerModel {
    private final String buyerID;
    private final String username;
    private final String email;
    private final String location;
    private final Set<String> itemsInterested;
    private final Set<String> messages;
    private final String signupDate;

    private BuyerModel(String buyerID, String username, String email, String location,
                       Set<String> itemsInterested, Set<String> messages, String signupDate) {
        this.buyerID = buyerID;
        this.username = username;
        this.email = email;
        this.location = location;
        this.itemsInterested = itemsInterested;
        this.messages = messages;
        this.signupDate = signupDate;
    }

    // Getters

    public String getBuyerID() {
        return buyerID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public Set<String> getItemsInterested() {
        return itemsInterested;
    }

    public Set<String> getMessages() {
        return messages;
    }

    public String getSignupDate() {
        return signupDate;
    }

    // equals() and hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyerModel that = (BuyerModel) o;
        return Objects.equals(buyerID, that.buyerID) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(location, that.location) &&
                Objects.equals(itemsInterested, that.itemsInterested) &&
                Objects.equals(messages, that.messages) &&
                Objects.equals(signupDate, that.signupDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerID, username, email, location, itemsInterested, messages, signupDate);
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String buyerID;
        private String username;
        private String email;
        private String location;
        private Set<String> itemsInterested;
        private Set<String> messages;
        private String signupDate;

        public Builder withBuyerID(String buyerID) {
            this.buyerID = buyerID;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder withItemsInterested(Set<String> itemsInterested) {
            this.itemsInterested = itemsInterested;
            return this;
        }

        public Builder withMessages(Set<String> messages) {
            this.messages = messages;
            return this;
        }

        public Builder withSignupDate(String signupDate) {
            this.signupDate = signupDate;
            return this;
        }

        public BuyerModel build() {
            return new BuyerModel(buyerID, username, email, location, itemsInterested, messages, signupDate);
        }
    }
}