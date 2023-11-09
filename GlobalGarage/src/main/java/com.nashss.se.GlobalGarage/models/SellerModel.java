package com.nashss.se.GlobalGarage.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class SellerModel {
    private final String sellerID;
    private final String username;
    private final String email;
    private final String location;
    private final Set<String> garages;
    private final Set<String> messages;
    private final String contactInfo;
    private String signupDate;

    private SellerModel(String sellerID, String username, String email, String location,
                        Set<String> garages, Set<String> messages, String contactInfo,
                        String signupDate) {
        this.sellerID = sellerID;
        this.username = username;
        this.email = email;
        this.location = location;
        this.garages = garages;
        this.messages = messages;
        this.contactInfo = contactInfo;
        this.signupDate = signupDate;
    }

    // Getters

    public String getSellerID() {
        return sellerID;
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

    public Set<String> getGarages() {
        return garages;
    }

    public Set<String> getMessages() {
        return messages;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String  getSignupDate() {
        return signupDate;
    }

    // equals() and hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerModel that = (SellerModel) o;
        return Objects.equals(sellerID, that.sellerID) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(location, that.location) &&
                Objects.equals(garages, that.garages) &&
                Objects.equals(messages, that.messages) &&
                Objects.equals(contactInfo, that.contactInfo) &&
                Objects.equals(signupDate, that.signupDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerID, username, email, location, garages, messages, contactInfo, signupDate);
    }

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sellerID;
        private String username;
        private String email;
        private String location;
        private Set<String> garages;
        private Set<String> messages;
        private String contactInfo;
        private String signupDate;

        public Builder withSellerID(String sellerID) {
            this.sellerID = sellerID;
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

        public Builder withGarages(Set<String> garages) {
            this.garages = garages;
            return this;
        }

        public Builder withMessages(Set<String> messages) {
            this.messages = messages;
            return this;
        }

        public Builder withContactInfo(String contactInfo) {
            this.contactInfo = contactInfo;
            return this;
        }

        public Builder withSignupDate(String signupDate) {
            this.signupDate = signupDate;
            return this;
        }

        public SellerModel build() {
            return new SellerModel(sellerID, username, email, location, garages, messages, contactInfo, signupDate);
        }
    }
}