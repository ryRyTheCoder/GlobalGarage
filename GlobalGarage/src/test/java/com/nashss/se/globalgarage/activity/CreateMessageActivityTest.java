package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.CreateMessageActivity;
import com.nashss.se.GlobalGarage.activity.request.CreateMessageRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateMessageResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.MessageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Message;
import com.nashss.se.GlobalGarage.models.MessageModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

public class CreateMessageActivityTest {
    @Mock
    private MessageDAO messageDao;
    @Mock
    private ModelConverter modelConverter;

    private CreateMessageActivity createMessageActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createMessageActivity = new CreateMessageActivity(messageDao, modelConverter);
    }

    @Test
    public void handleRequest_validRequest_createsMessage() {
        // GIVEN
        CreateMessageRequest request = createSampleMessageRequest();
        Message dummyMessage = createSampleMessageFromRequest(request);
        MessageModel dummyMessageModel = createSampleMessageModelFromMessage(dummyMessage);

        Mockito.when(messageDao.createMessage(any(Message.class))).thenReturn(true);
        Mockito.when(messageDao.updateUserMessages(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        Mockito.when(modelConverter.toMessageModel(any(Message.class))).thenReturn(dummyMessageModel);

        // WHEN
        CreateMessageResult result = createMessageActivity.handleRequest(request);

        // THEN
        assertTrue(result.isSuccess());
        assertNotNull(result.getMessageModel());
        assertEquals("Message created successfully.", result.getMessage());

        // Capture the actual Message object passed to messageDao.createMessage
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageDao).createMessage(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();

        // Assert the static fields of the Message object
        assertEquals(request.getRelatedItemID(), capturedMessage.getRelatedItemID());
        assertEquals(request.getSenderType(), capturedMessage.getSenderType());
        assertEquals(request.getSenderID(), capturedMessage.getSenderID());
        assertEquals(request.getReceiverType(), capturedMessage.getReceiverType());
        assertEquals(request.getReceiverID(), capturedMessage.getReceiverID());
        assertEquals(request.getContent(), capturedMessage.getContent());
        // Timestamp and messageID are dynamic and are not asserted here
    }
    // Additional helper methods...

    private CreateMessageRequest createSampleMessageRequest() {
        return new CreateMessageRequest(
                "item123",
                "seller",
                "seller123",
                "buyer",
                "buyer123",
                "Hello, I'm interested in your item."
        );
    }

    private Message createSampleMessageFromRequest(CreateMessageRequest request) {
        Message message = new Message();
        message.setMessageID(UUID.randomUUID().toString());
        message.setRelatedItemID(request.getRelatedItemID());
        message.setSenderType(request.getSenderType());
        message.setSenderID(request.getSenderID());
        message.setReceiverType(request.getReceiverType());
        message.setReceiverID(request.getReceiverID());
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());
        return message;
    }

    private MessageModel createSampleMessageModelFromMessage(Message message) {
        return new MessageModel(
                message.getMessageID(),
                message.getRelatedItemID(),
                message.getSenderType(),
                message.getSenderID(),
                message.getReceiverType(),
                message.getReceiverID(),
                message.getContent(),
                message.getTimestamp().toString()
        );
    }
}
