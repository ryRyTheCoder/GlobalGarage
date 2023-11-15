package com.nashss.se.GlobalGarage.activity.results;

import com.nashss.se.GlobalGarage.models.MessageModel;

import java.util.Objects;

public class CreateMessageResult {
    private final boolean success;
    private final String message;
    private final MessageModel messageModel;

    private CreateMessageResult(Builder builder) {
        this.success = builder.success;
        this.message = builder.message;
        this.messageModel = builder.messageModel;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    @Override
    public String toString() {
        return "CreateMessageResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", messageModel=" + messageModel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateMessageResult that = (CreateMessageResult) o;
        return success == that.success && Objects.equals(message, that.message) && Objects.equals(messageModel, that.messageModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, messageModel);
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String message;
        private MessageModel messageModel;

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withMessageModel(MessageModel messageModel) {
            this.messageModel = messageModel;
            return this;
        }

        public CreateMessageResult build() {
            return new CreateMessageResult(this);
        }
    }

}