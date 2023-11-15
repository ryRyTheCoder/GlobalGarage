package com.nashss.se.GlobalGarage.activity.request;

import java.util.Objects;

public class CreateMessageRequest {
    private  String relatedItemID;
    private  String senderType;
    private  String senderID;
    private  String receiverType;
    private  String receiverID;
    private  String content;


    private CreateMessageRequest(String relatedItemID, String senderType, String senderID,
                                 String receiverType, String receiverID, String content) {
        this.relatedItemID = relatedItemID;
        this.senderType = senderType;
        this.senderID = senderID;
        this.receiverType = receiverType;
        this.receiverID = receiverID;
        this.content = content;
    }

    // No-argument default constructor
    public CreateMessageRequest() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateMessageRequest that = (CreateMessageRequest) o;
        return Objects.equals(relatedItemID, that.relatedItemID) && Objects.equals(senderType, that.senderType) && Objects.equals(senderID, that.senderID) && Objects.equals(receiverType, that.receiverType) && Objects.equals(receiverID, that.receiverID) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relatedItemID, senderType, senderID, receiverType, receiverID, content);
    }

    @Override
    public String toString() {
        return "CreateMessageRequest{" +
                "relatedItemID='" + relatedItemID + '\'' +
                ", senderType='" + senderType + '\'' +
                ", senderID='" + senderID + '\'' +
                ", receiverType='" + receiverType + '\'' +
                ", receiverID='" + receiverID + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String relatedItemID;
        private String senderType;
        private String senderID;
        private String receiverType;
        private String receiverID;
        private String content;

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

        public CreateMessageRequest build() {
            return new CreateMessageRequest(relatedItemID, senderType, senderID, receiverType, receiverID, content);
        }
    }
}