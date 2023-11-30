package com.nashss.se.GlobalGarage.dynamodb.models;

import com.nashss.se.GlobalGarage.converters.LocalDateTimeConverter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@DynamoDBTable(tableName = "sellers")
public class Seller {
    private String sellerID;
    private String username;
    private String email;
    private String location;
    private Set<String> garages;
    private Set<String> messages;
    private String contactInfo;
    private LocalDateTime signupDate;

    @DynamoDBHashKey(attributeName = "sellerID")
    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @DynamoDBAttribute(attributeName = "garages")
    public Set<String> getGarages() {
        return garages;
    }

    public void setGarages(Set<String> garages) {
        this.garages = garages;
    }

    @DynamoDBAttribute(attributeName = "messages")
    public Set<String> getMessages() {
        return messages;
    }

    public void setMessages(Set<String> messages) {
        this.messages = messages;
    }

    @DynamoDBAttribute(attributeName = "contactInfo")
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "signupDate")
    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seller seller = (Seller) o;
        return Objects.equals(sellerID, seller.sellerID) &&
                Objects.equals(username, seller.username) &&
                Objects.equals(email, seller.email) &&
                Objects.equals(location, seller.location) &&
                Objects.equals(garages, seller.garages) &&
                Objects.equals(messages, seller.messages) &&
                Objects.equals(contactInfo, seller.contactInfo) &&
                Objects.equals(signupDate, seller.signupDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerID, username, email, location, garages, messages, contactInfo, signupDate);
    }

    @Override
    public String toString() {
        return "Seller{" +
                "sellerID='" + sellerID + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", events=" + garages +
                ", messages=" + messages +
                ", contactInfo='" + contactInfo + '\'' +
                ", signupDate=" + signupDate +
                '}';
    }
}
