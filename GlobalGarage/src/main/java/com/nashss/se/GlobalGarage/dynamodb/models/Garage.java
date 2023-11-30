package com.nashss.se.GlobalGarage.dynamodb.models;

import com.nashss.se.GlobalGarage.converters.LocalDateTimeConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "garages")
public class Garage {
    private String sellerID;
    private String garageID;
    private String garageName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String description;
    private List<String> items;
    private Boolean isActive;

    @DynamoDBHashKey(attributeName = "sellerID")
    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    @DynamoDBRangeKey(attributeName = "garageID")
    public String getGarageID() {
        return garageID;
    }

    public void setGarageID(String garageID) {
        this.garageID = garageID;
    }

    @DynamoDBAttribute(attributeName = "garageName")
    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "startDate")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "endDate")
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @DynamoDBAttribute(attributeName = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "items")
    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @DynamoDBAttribute(attributeName = "isActive")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // equals, hashCode, and toString methods


    @Override
    public String toString() {
        return "Garage{" +
                "sellerID='" + sellerID + '\'' +
                ", garageID='" + garageID + '\'' +
                ", garageName='" + garageName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", items=" + items +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Garage garage = (Garage) o;
        return Objects.equals(sellerID, garage.sellerID) && Objects.equals(garageID, garage.garageID) &&
                Objects.equals(garageName, garage.garageName) && Objects.equals(startDate, garage.startDate) &&
                Objects.equals(endDate, garage.endDate) && Objects.equals(location, garage.location) &&
                Objects.equals(description, garage.description) && Objects.equals(items, garage.items) &&
                Objects.equals(isActive, garage.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerID, garageID, garageName, startDate, endDate, location,
                description, items, isActive);
    }

}
