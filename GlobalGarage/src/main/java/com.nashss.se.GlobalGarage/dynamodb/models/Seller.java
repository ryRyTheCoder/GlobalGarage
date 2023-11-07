package com.nashss.se.GlobalGarage.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.time.LocalDateTime;

import java.util.Objects;
import java.util.Set;

@DynamoDBTable(tableName = "Sellers")
public class Seller {
    private String sellerID;
    private String username;
    private String email;
    private String location;
    private Set<String> events;
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

    @DynamoDBAttribute(attributeName = "events")
    public Set<String> getEvents() {
        return events;
    }

    public void setEvents(Set<String> events) {
        this.events = events;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(sellerID, seller.sellerID) && Objects.equals(username, seller.username) && Objects.equals(email, seller.email) && Objects.equals(location, seller.location) && Objects.equals(events, seller.events) && Objects.equals(messages, seller.messages) && Objects.equals(contactInfo, seller.contactInfo) && Objects.equals(signupDate, seller.signupDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerID, username, email, location, events, messages, contactInfo, signupDate);
    }

    @Override
    public String toString() {
        return "Seller{" +
                "sellerID='" + sellerID + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", events=" + events +
                ", messages=" + messages +
                ", contactInfo='" + contactInfo + '\'' +
                ", signupDate=" + signupDate +
                '}';
    }

    // Custom converter for LocalDateTime, as DynamoDB doesn't support LocalDateTime natively
    public static class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
        @Override
        public String convert(final LocalDateTime time) {
            return time.toString();
        }

        @Override
        public LocalDateTime unconvert(final String stringValue) {
            return LocalDateTime.parse(stringValue);
        }
    }
}
