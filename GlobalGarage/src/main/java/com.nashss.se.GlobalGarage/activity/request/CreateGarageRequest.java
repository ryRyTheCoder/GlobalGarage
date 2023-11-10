package com.nashss.se.GlobalGarage.activity.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

/**
 * This class represents a request to create a garage.
 * It is used as a part of the CreateGarageActivity API.
 */
@JsonDeserialize(builder = CreateGarageRequest.Builder.class)
public class CreateGarageRequest {
    private final String sellerID;
    private final String garageName;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String location;
    private final String description;

    private CreateGarageRequest(String sellerID, String garageName, LocalDateTime startDate, LocalDateTime endDate,
                                String location, String description) {
        this.sellerID = sellerID;
        this.garageName = garageName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
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
    public String getSellerID() {
        return sellerID;
    }
    @Override
    public String toString() {
        return "CreateGarageRequest{" +
                "sellerID='" + sellerID + '\'' +
                ", garageName='" + garageName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sellerID;
        private String garageName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String location;
        private String description;

        public Builder withSellerID(String sellerID) { // Add method for sellerID
            this.sellerID = sellerID;
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

        public CreateGarageRequest build() {
            return new CreateGarageRequest(sellerID, garageName, startDate, endDate, location, description);
        }
    }
}