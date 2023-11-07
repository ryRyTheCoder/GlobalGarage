package com.nashss.se.GlobalGarage.dynamodb.models;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.nashss.se.GlobalGarage.converters.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "Messages")
public class Message {
    private String messageID;
    private String relatedItemID;
    private String senderType;
    private String senderID;
    private String receiverType;
    private String receiverID;
    private String content;
    private LocalDateTime timestamp;

    @DynamoDBHashKey(attributeName = "messageID")
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @DynamoDBRangeKey(attributeName = "relatedItemID")
    public String getRelatedItemID() {
        return relatedItemID;
    }

    public void setRelatedItemID(String relatedItemID) {
        this.relatedItemID = relatedItemID;
    }

    @DynamoDBAttribute(attributeName = "senderType")
    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    @DynamoDBAttribute(attributeName = "senderID")
    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    @DynamoDBAttribute(attributeName = "receiverType")
    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    @DynamoDBAttribute(attributeName = "receiverID")
    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    @DynamoDBAttribute(attributeName = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBAttribute(attributeName = "timestamp")
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // equals, hashCode, and toString methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return Objects.equals(messageID, that.messageID) &&
                Objects.equals(relatedItemID, that.relatedItemID) &&
                Objects.equals(senderType, that.senderType) &&
                Objects.equals(senderID, that.senderID) &&
                Objects.equals(receiverType, that.receiverType) &&
                Objects.equals(receiverID, that.receiverID) &&
                Objects.equals(content, that.content) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageID, relatedItemID, senderType, senderID, receiverType, receiverID, content, timestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageID='" + messageID + '\'' +
                ", relatedItemID='" + relatedItemID + '\'' +
                ", senderType='" + senderType + '\'' +
                ", senderID='" + senderID + '\'' +
                ", receiverType='" + receiverType + '\'' +
                ", receiverID='" + receiverID + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
