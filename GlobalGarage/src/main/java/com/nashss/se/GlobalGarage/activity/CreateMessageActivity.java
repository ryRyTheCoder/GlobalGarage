package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.CreateMessageRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateMessageResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.MessageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Message;
import com.nashss.se.GlobalGarage.models.MessageModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.inject.Inject;

public class CreateMessageActivity {
    private final Logger log = LogManager.getLogger();
    private final MessageDAO messageDao;
    private final ModelConverter modelConverter;

    /**
     * Constructs a CreateMessageActivity instance.
     *
     * @param messageDao       The {@link MessageDAO} used for accessing and manipulating message data.
     * @param modelConverter   The {@link ModelConverter} used for converting between different model types.
     */

    @Inject
    public CreateMessageActivity(MessageDAO messageDao, ModelConverter modelConverter) {
        this.messageDao = messageDao;
        this.modelConverter = modelConverter;
    }

    /**
     * Handles the request to create a new message.
     * This method generates a unique message ID and timestamp, creates a new {@link Message} object,
     * and then stores it in the database using {@link MessageDAO}. It also updates the message lists
     * for both the sender and receiver.
     *
     * @param createMessageRequest The request object containing details needed to create a new message.
     * @return                     A {@link CreateMessageResult} object representing the outcome of the operation,
     *                             including success status, a message indicating the result, and the
     *                             created message model.
     */

    public CreateMessageResult handleRequest(final CreateMessageRequest createMessageRequest) {
        log.info("Received CreateMessageRequest {}", createMessageRequest);

        String messageID = UUID.randomUUID().toString();
        LocalDateTime timestamp = LocalDateTime.now();

        Message message = new Message();
        message.setMessageID(messageID);
        message.setRelatedItemID(createMessageRequest.getRelatedItemID());
        message.setSenderType(createMessageRequest.getSenderType());
        message.setSenderID(createMessageRequest.getSenderID());
        message.setReceiverType(createMessageRequest.getReceiverType());
        message.setReceiverID(createMessageRequest.getReceiverID());
        message.setContent(createMessageRequest.getContent());
        message.setTimestamp(timestamp);

        boolean success = messageDao.createMessage(message);

        if (success) {
            // Update both sender's and receiver's account messages
            success = messageDao.updateUserMessages(
                    createMessageRequest.getSenderID(), createMessageRequest.getSenderType(),
                    createMessageRequest.getReceiverID(), createMessageRequest.getReceiverType(),
                    messageID
            );
        }

        String messageText = success ? "Message created successfully." : "Failed to create message.";

        MessageModel messageModel = modelConverter.toMessageModel(message);

        return CreateMessageResult.builder()
                .withSuccess(success)
                .withMessage(messageText)
                .withMessageModel(messageModel)
                .build();
    }
}
