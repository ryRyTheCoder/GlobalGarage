package com.nashss.se.GlobalGarage.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


/**
 * This class represents a request to create a seller.
 * It is used as a part of the CreateSellerActivity API.
 */
@JsonDeserialize(builder = CreateSellerRequest.Builder.class)
public class CreateSellerRequest {
    private final String sellerId;
    private final String username;
    private final String email;
    private final String location;
    private final String contactInfo;

    private CreateSellerRequest(String sellerId, String username, String email, String location, String contactInfo) {
        this.sellerId = sellerId;
        this.username = username;
        this.email = email;
        this.location = location;
        this.contactInfo = contactInfo;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public String getSellerId() {
        return sellerId;
    }

    @Override
    public String toString() {
        return "CreateSellerRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sellerId;
        private String username;
        private String email;
        private String location;
        private String contactInfo;

        public Builder withSellerId(String sellerId) {
            this.sellerId = sellerId;
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

        public Builder withContactInfo(String contactInfo) {
            this.contactInfo = contactInfo;
            return this;
        }

        public CreateSellerRequest build() {
            return new CreateSellerRequest(sellerId, username, email, location, contactInfo);
        }
    }
}
