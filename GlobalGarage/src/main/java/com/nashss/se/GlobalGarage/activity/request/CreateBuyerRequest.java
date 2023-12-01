package com.nashss.se.GlobalGarage.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This class represents a request to create a buyer.
 */
@JsonDeserialize(builder = CreateBuyerRequest.Builder.class)
public class CreateBuyerRequest {
    private final String buyerId;
    private final String username;
    private final String email;
    private final String location;
    private final String contactInfo;

    /**
     * Private constructor to create a new CreateBuyerRequest.
     *
     * @param buyerId     The unique identifier for the buyer.
     * @param username    The username of the buyer.
     * @param email       The email address of the buyer.
     * @param location    The location of the buyer.
     * @param contactInfo The contact information for the buyer.
     */

    public CreateBuyerRequest(String buyerId, String username, String email, String location, String contactInfo) {
        this.buyerId = buyerId;
        this.username = username;
        this.email = email;
        this.location = location;
        this.contactInfo = contactInfo;
    }
    /**
     * Gets the buyer ID.
     *
     * @return The buyer ID.
     */
    public String getBuyerId() {
        return buyerId;
    }
    /**
     * Gets the username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Gets the email address.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Gets the location.
     *
     * @return The location.
     */
    public String getLocation() {
        return location;
    }
    /**
     * Gets the contact information.
     *
     * @return The contact information.
     */
    public String getContactInfo() {
        return contactInfo;
    }
    /**
     * Returns a string representation of the CreateBuyerRequest.
     *
     * @return A string representation of the CreateBuyerRequest.
     */
    @Override
    public String toString() {
        return "CreateBuyerRequest{" +
                "buyerId='" + buyerId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }

    /**
     * Creates a new Builder for CreateBuyerRequest.
     *
     * @return A new Builder instance.
     */
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String buyerId;
        private String username;
        private String email;
        private String location;
        private String contactInfo;

        public Builder withBuyerId(String buyerId) {
            this.buyerId = buyerId;
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

        /**
         * Builds the CreateBuyerRequest instance.
         * @return the created CreateBuyerRequest instance.
         */

        public CreateBuyerRequest build() {
            return new CreateBuyerRequest(buyerId, username, email, location, contactInfo);
        }
    }
}
