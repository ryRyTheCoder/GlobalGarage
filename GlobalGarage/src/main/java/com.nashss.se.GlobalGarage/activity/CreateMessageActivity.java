package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.CreateMessageRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateMessageResult;
import com.nashss.se.GlobalGarage.dynamodb.MessageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Message;
import com.nashss.se.GlobalGarage.models.MessageModel;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreateMessageActivity {
    private final Logger log = LogManager.getLogger();
    private final MessageDAO messageDao;
    private final ModelConverter modelConverter;

    @Inject
    public CreateMessageActivity(MessageDAO messageDao, ModelConverter modelConverter) {
        this.messageDao = messageDao;
        this.modelConverter = modelConverter;
    }

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