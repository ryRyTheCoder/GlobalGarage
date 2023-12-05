package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.GetBuyerActivity;
import com.nashss.se.GlobalGarage.activity.request.GetBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetBuyerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.models.BuyerModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;

public class GetBuyerActivityTest {
    @Mock
    private BuyerDAO buyerDao;
    @Mock
    private ModelConverter modelConverter;

    private GetBuyerActivity getBuyerActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getBuyerActivity = new GetBuyerActivity(buyerDao, modelConverter);
    }

    @Test
    public void handleRequest_BuyerFound() {
        // GIVEN
        String buyerId = "buyer123";
        GetBuyerRequest request = new GetBuyerRequest(buyerId);

        Buyer buyer = createSampleBuyer(buyerId);
        BuyerModel expectedBuyerModel = createSampleBuyerModel(buyer);

        Mockito.when(buyerDao.getBuyer(buyerId)).thenReturn(buyer);
        Mockito.when(modelConverter.toBuyerModel(buyer)).thenReturn(expectedBuyerModel);

        // WHEN
        GetBuyerResult result = getBuyerActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Buyer fetched successfully.", result.getMessage());
        assertEquals(expectedBuyerModel, result.getBuyerModel());
    }

    @Test
    public void handleRequest_BuyerNotFound() {
        // GIVEN
        String buyerId = "nonexistentBuyer";
        GetBuyerRequest request = new GetBuyerRequest(buyerId);

        Mockito.when(buyerDao.getBuyer(buyerId)).thenReturn(null);

        // WHEN
        GetBuyerResult result = getBuyerActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Buyer not found.", result.getMessage());
        assertNull(result.getBuyerModel());
    }

    private Buyer createSampleBuyer(String buyerId) {
        Buyer buyer = new Buyer();
        buyer.setBuyerID(buyerId);
        buyer.setUsername("TestBuyer");
        buyer.setEmail("testbuyer@example.com");
        buyer.setLocation("123 Buyer St");
        buyer.setItemsInterested(new HashSet<>());
        buyer.setMessages(new HashSet<>());
        buyer.setSignupDate(LocalDateTime.now());
        return buyer;
    }

    private BuyerModel createSampleBuyerModel(Buyer buyer) {
        return new BuyerModel(
                buyer.getBuyerID(),
                buyer.getUsername(),
                buyer.getEmail(),
                buyer.getLocation(),
                buyer.getItemsInterested(),
                buyer.getMessages(),
                buyer.getSignupDate().toString()
        );
    }
}
