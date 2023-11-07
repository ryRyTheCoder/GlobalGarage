package com.nashss.se.GlobalGarage.models;


import java.time.LocalDateTime;
import java.util.Objects;

public class MessageModel {
    private final String messageID;
    private final String relatedItemID;
    private final String senderType;
    private final String senderID;
    private final String receiverType;
    private final String receiverID;
    private final String content;
    private final LocalDateTime timestamp;

    private MessageModel(String messageID, String relatedItemID, String senderType,
                          String senderID, String receiverType, String receiverID,
                          String content, LocalDateTime timestamp) {
        this.messageID = messageID;
        this.relatedItemID = relatedItemID;
        this.senderType = senderType;
        this.senderID = senderID;
        this.receiverType = receiverType;
        this.receiverID = receiverID;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters

    public String getMessageID() {
        return messageID;
    }

    public String getRelatedItemID() {
        return relatedItemID;
    }

    public String getSenderType() {
        return senderType;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // equals() and hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageModel that = (MessageModel) o;
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

    // Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String messageID;
        private String relatedItemID;
        private String senderType;
        private String senderID;
        private String receiverType;
        private String receiverID;
        private String content;
        private LocalDateTime timestamp;

        public Builder withMessageID(String messageID) {
            this.messageID = messageID;
            return this;
        }

        public Builder withRelatedItemID(String relatedItemID) {
            this.relatedItemID = relatedItemID;
            return this;
        }

        public Builder withSenderType(String senderType) {
            this.senderType = senderType;
            return this;
        }

        public Builder withSenderID(String senderID) {
            this.senderID = senderID;
            return this;
        }

        public Builder withReceiverType(String receiverType) {
            this.receiverType = receiverType;
            return this;
        }

        public Builder withReceiverID(String receiverID) {
            this.receiverID = receiverID;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MessageModel build() {
            return new MessageModel(messageID, relatedItemID, senderType, senderID, receiverType, receiverID, content, timestamp);
        }
    }
}
