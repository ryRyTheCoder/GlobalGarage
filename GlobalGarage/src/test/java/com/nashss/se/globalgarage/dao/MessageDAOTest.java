package com.nashss.se.globalgarage.dao;

import com.nashss.se.GlobalGarage.dynamodb.MessageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.dynamodb.models.Message;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.exceptions.MessageNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

class MessageDAOTest {

    @InjectMocks
    private MessageDAO messageDAO;

    @Mock
    private DynamoDBMapper mockMapper;
    @Mock
    private MetricsPublisher mockMetricsPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMessage_SuccessfulCreation_ReturnsTrue() {
        // Arrange
        Message message = new Message();
        // Set message properties here

        // Act
        boolean result = messageDAO.createMessage(message);

        // Assert
        assertTrue(result);
        verify(mockMapper).save(message);
        verify(mockMetricsPublisher).addCount(MetricsConstants.CREATE_MESSAGE_SUCCESS_COUNT, 1);
    }

    @Test
    void createMessage_NullMessage_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> messageDAO.createMessage(null));
    }

    @Test
    void getMessage_MessageExists_ReturnsMessage() {
        // Arrange
        String messageID = "message123";
        Message expectedMessage = new Message();
        when(mockMapper.load(Message.class, messageID)).thenReturn(expectedMessage);

        // Act
        Message result = messageDAO.getMessage(messageID);

        // Assert
        assertNotNull(result);
        assertEquals(expectedMessage, result);
    }

    @Test
    void getMessage_MessageNotFound_ThrowsMessageNotFoundException() {
        // Arrange
        String messageID = "nonexistentMessage";
        when(mockMapper.load(Message.class, messageID)).thenReturn(null);

        // Act & Assert
        assertThrows(MessageNotFoundException.class, () -> messageDAO.getMessage(messageID));
    }
    @Test
    void updateUserMessages_SuccessfulUpdate_ReturnsTrue() {
        // Arrange
        String senderID = "sender123";
        String senderType = "buyer"; // or "seller"
        String receiverID = "receiver123";
        String receiverType = "seller"; // or "buyer"
        String messageID = "message123";

        // Mocking behavior for both buyer and seller
        Buyer mockBuyer = mock(Buyer.class);
        Seller mockSeller = mock(Seller.class);
        when(mockMapper.load(Buyer.class, senderID)).thenReturn(mockBuyer);
        when(mockMapper.load(Seller.class, receiverID)).thenReturn(mockSeller);

        // Act
        boolean result = messageDAO.updateUserMessages(senderID, senderType, receiverID, receiverType, messageID);

        // Assert
        assertTrue(result);
        verify(mockMapper).save(mockBuyer);
        verify(mockMapper).save(mockSeller);
    }

    @Test
    void updateUserMessages_UserNotFound_ReturnsFalse() {
        // Arrange
        String senderID = "nonexistentSender";
        String senderType = "buyer";
        String receiverID = "receiver123";
        String receiverType = "seller";
        String messageID = "message123";

        when(mockMapper.load(Buyer.class, senderID)).thenReturn(null);
        when(mockMapper.load(Seller.class, receiverID)).thenReturn(null);

        // Act
        boolean result = messageDAO.updateUserMessages(senderID, senderType, receiverID, receiverType, messageID);

        // Assert
        assertFalse(result);
    }
}
