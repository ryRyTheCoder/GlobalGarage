package com.nashss.se.GlobalGarage.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Represents a request to update a buyer's information.
 */
@JsonDeserialize(builder = UpdateBuyerRequest.Builder.class)
public class UpdateBuyerRequest {
    private final String buyerID;
    private final String username;
    private final String email;
    private final String location;

    /**
     * Creates a new instance of UpdateBuyerRequest.
     * This constructor is private and only used by the Builder class to create an instance.
     *
     * @param buyerID The unique identifier of the buyer.
     * @param username The updated username of the buyer.
     * @param email The updated email address of the buyer.
     * @param location The updated location of the buyer.
     */

    private UpdateBuyerRequest(String buyerID, String username, String email, String location) {
        this.buyerID = buyerID;
        this.username = username;
        this.email = email;
        this.location = location;
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


    @Override
    public String toString() {
        return "UpdateBuyerRequest{" +
                "buyerID='" + buyerID + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String buyerID;
        private String username;
        private String email;
        private String location;


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


        public UpdateBuyerRequest build() {
            return new UpdateBuyerRequest(buyerID, username, email, location);

        }
    }
}
