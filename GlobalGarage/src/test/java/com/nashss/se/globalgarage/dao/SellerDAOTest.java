package com.nashss.se.globalgarage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.exceptions.SellerNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

class SellerDAOTest {

    @Mock
    private DynamoDBMapper mockMapper;
    @Mock
    private MetricsPublisher mockMetricsPublisher;

    private SellerDAO sellerDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerDAO = new SellerDAO(mockMapper, mockMetricsPublisher);
    }

    @Test
    void createSeller_ValidSeller_ReturnsTrue() {
        Seller validSeller = createSampleSeller();
        doNothing().when(mockMapper).save(any(Seller.class));
        assertTrue(sellerDAO.createSeller(validSeller));
        verify(mockMetricsPublisher).addCount(MetricsConstants.CREATE_SELLER_SUCCESS_COUNT, 1);
    }

    @Test
    void createSeller_NullSeller_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> sellerDAO.createSeller(null));
    }

    @Test
    void getSeller_SellerExists_ReturnsSeller() {
        String sellerId = "seller123";
        Seller existingSeller = createSampleSeller();
        when(mockMapper.load(Seller.class, sellerId)).thenReturn(existingSeller);
        Seller result = sellerDAO.getSeller(sellerId);
        assertEquals(existingSeller, result);
    }

    @Test
    void getSeller_SellerNotFound_ThrowsSellerNotFoundException() {
        String sellerId = "nonExistingSeller";
        when(mockMapper.load(Seller.class, sellerId)).thenReturn(null);
        assertThrows(SellerNotFoundException.class, () -> sellerDAO.getSeller(sellerId));
    }

    @Test
    void updateSeller_ValidSeller_ReturnsTrue() {
        Seller validSeller = createSampleSeller();
        doNothing().when(mockMapper).save(any(Seller.class));
        assertTrue(sellerDAO.updateSeller(validSeller));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_SELLER_SUCCESS_COUNT, 1);
    }

    @Test
    void updateSeller_FailedUpdate_ReturnsFalse() {
        Seller validSeller = createSampleSeller();
        doThrow(new RuntimeException()).when(mockMapper).save(any(Seller.class));
        assertFalse(sellerDAO.updateSeller(validSeller));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_SELLER_FAIL_COUNT, 1);
    }

    private Seller createSampleSeller() {
        Seller seller = new Seller();
        seller.setSellerID("seller123");
        seller.setUsername("sellerUser");
        seller.setEmail("seller@example.com");
        seller.setLocation("123 Seller Street");
        seller.setGarages(new HashSet<>(Set.of("garage1", "garage2")));
        seller.setMessages(new HashSet<>(Set.of("message1", "message2")));
        seller.setContactInfo("Contact Info");
        seller.setSignupDate(LocalDateTime.now());
        return seller;
    }
}
