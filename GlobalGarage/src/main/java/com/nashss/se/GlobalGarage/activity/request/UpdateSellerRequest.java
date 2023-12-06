package com.nashss.se.GlobalGarage.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Set;

/**
 * Represents a request to update a seller's information.
 */
@JsonDeserialize(builder = UpdateSellerRequest.Builder.class)
public class UpdateSellerRequest {
    private final String sellerID;
    private final String username;
    private final String email;
    private final String location;
    private final Set<String> garages;
    private final Set<String> messages;
    private final String contactInfo;

    /**
     * Constructs an UpdateSellerRequest with the provided details.
     *
     * @param sellerID The ID of the seller.
     * @param username The updated username.
     * @param email The updated email address.
     * @param location The updated location.
     * @param garages The updated set of garages.
     * @param messages The updated set of messages.
     * @param contactInfo The updated contact information.
     */
    private UpdateSellerRequest(String sellerID, String username, String email,
                                String location, Set<String> garages, Set<String> messages, String contactInfo) {
        this.sellerID = sellerID;
        this.username = username;
        this.email = email;
        this.location = location;
        this.garages = garages;
        this.messages = messages;
        this.contactInfo = contactInfo;
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

    @Override
    public String toString() {
        return "UpdateSellerRequest{" +
                "sellerID='" + sellerID + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", garages=" + garages +
                ", messages=" + messages +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String sellerID;
        private String username;
        private String email;
        private String location;
        private Set<String> garages;
        private Set<String> messages;
        private String contactInfo;

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

        public UpdateSellerRequest build() {
            return new UpdateSellerRequest(sellerID, username, email, location, garages, messages, contactInfo);
        }
    }
}
