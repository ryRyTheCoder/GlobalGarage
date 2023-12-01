package com.nashss.se.GlobalGarage.models;

import java.util.Objects;
import java.util.Set;

/**
 * Represents a model of a seller on the platform.
 * This class encapsulates the seller's details including their ID, username, email,
 * location, associated garages, messages, contact information, and the date of signup.
 */

public class SellerModel {
    private final String sellerID;
    private final String username;
    private final String email;
    private final String location;
    private final Set<String> garages;
    private final Set<String> messages;
    private final String contactInfo;
    private final String signupDate;

    /**
     * Constructs a new instance of SellerModel with detailed seller information.
     * This constructor initializes the seller model with specific attributes related to
     * the seller's identity and activities on the platform.
     *
     * @param sellerID     The unique identifier of the seller.
     * @param username     The username of the seller.
     * @param email        The email address of the seller.
     * @param location     The geographic location of the seller.
     * @param garages      A set of garage IDs associated with the seller.
     * @param messages     A set of message IDs related to the seller.
     * @param contactInfo  The contact information of the seller.
     * @param signupDate   The date when the seller signed up on the platform.
     */

    public SellerModel(String sellerID, String username, String email, String location,
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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

    //CHECKSTYLE:OFF:Builder

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
