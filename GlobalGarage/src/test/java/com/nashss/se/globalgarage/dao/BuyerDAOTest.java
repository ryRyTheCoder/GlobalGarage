package com.nashss.se.globalgarage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.exceptions.BuyerNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

class BuyerDAOTest {

    @Mock
    private DynamoDBMapper mockMapper;
    @Mock
    private MetricsPublisher mockMetricsPublisher;

    private BuyerDAO buyerDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buyerDAO = new BuyerDAO(mockMapper, mockMetricsPublisher);
    }

    @Test
    void createBuyer_ValidBuyer_ReturnsTrue() {
        Buyer validBuyer = createSampleBuyer();
        doNothing().when(mockMapper).save(any(Buyer.class));
        assertTrue(buyerDAO.createBuyer(validBuyer));
        verify(mockMetricsPublisher).addCount(MetricsConstants.CREATE_BUYER_SUCCESS_COUNT, 1);
    }

    @Test
    void createBuyer_NullBuyer_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> buyerDAO.createBuyer(null));
    }

    @Test
    void getBuyer_BuyerExists_ReturnsBuyer() {
        String buyerId = "buyer123";
        Buyer existingBuyer = createSampleBuyer();
        when(mockMapper.load(Buyer.class, buyerId)).thenReturn(existingBuyer);
        Buyer result = buyerDAO.getBuyer(buyerId);
        assertEquals(existingBuyer, result);
    }

    @Test
    void getBuyer_BuyerNotFound_ThrowsBuyerNotFoundException() {
        String buyerId = "nonExistingBuyer";
        when(mockMapper.load(Buyer.class, buyerId)).thenReturn(null);
        assertThrows(BuyerNotFoundException.class, () -> buyerDAO.getBuyer(buyerId));
    }

    @Test
    void updateBuyer_ValidBuyer_ReturnsTrue() {
        Buyer validBuyer = createSampleBuyer();
        doNothing().when(mockMapper).save(any(Buyer.class));
        assertTrue(buyerDAO.updateBuyer(validBuyer));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_BUYER_SUCCESS_COUNT, 1);
    }

    @Test
    void updateBuyer_FailedUpdate_ReturnsFalse() {
        Buyer validBuyer = createSampleBuyer();
        doThrow(new RuntimeException()).when(mockMapper).save(any(Buyer.class));
        assertFalse(buyerDAO.updateBuyer(validBuyer));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_BUYER_FAIL_COUNT, 1);
    }

    private Buyer createSampleBuyer() {
        Buyer buyer = new Buyer();
        buyer.setBuyerID("buyer123");
        buyer.setUsername("buyerUser");
        buyer.setEmail("buyer@example.com");
        buyer.setLocation("123 Buyer Street");
        buyer.setItemsInterested(new HashSet<>(Arrays.asList("item1", "item2")));
        buyer.setMessages(new HashSet<>(Arrays.asList("message1", "message2")));
        buyer.setSignupDate(LocalDateTime.now());
        return buyer;
    }
}
