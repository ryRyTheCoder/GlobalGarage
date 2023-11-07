package com.nashss.se.GlobalGarage.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.GlobalGarage.converters.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.Objects;

@DynamoDBTable(tableName = "Buyers")
public class Buyer {
    private String buyerID;
    private String username;
    private String email;
    private String location;
    private Set<String> itemsInterested;
    private Set<String> messages;
    private LocalDateTime signupDate;

    @DynamoDBHashKey(attributeName = "buyerID")
    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
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

    @DynamoDBAttribute(attributeName = "itemsInterested")
    public Set<String> getItemsInterested() {
        return itemsInterested;
    }

    public void setItemsInterested(Set<String> itemsInterested) {
        this.itemsInterested = itemsInterested;
    }

    @DynamoDBAttribute(attributeName = "messages")
    public Set<String> getMessages() {
        return messages;
    }

    public void setMessages(Set<String> messages) {
        this.messages = messages;
    }

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "signupDate")
    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    // equals, hashCode, and toString methods


    @Override
    public String toString() {
        return "Buyer{" +
                "buyerID='" + buyerID + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", itemsInterested=" + itemsInterested +
                ", messages=" + messages +
                ", signupDate=" + signupDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Buyer buyer = (Buyer) o;
        return Objects.equals(buyerID, buyer.buyerID) && Objects.equals(username, buyer.username) && Objects.equals(email, buyer.email) && Objects.equals(location, buyer.location) && Objects.equals(itemsInterested, buyer.itemsInterested) && Objects.equals(messages, buyer.messages) && Objects.equals(signupDate, buyer.signupDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerID, username, email, location, itemsInterested, messages, signupDate);
    }


}
