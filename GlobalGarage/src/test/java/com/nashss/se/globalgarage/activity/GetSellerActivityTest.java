package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.GetSellerActivity;
import com.nashss.se.GlobalGarage.activity.request.GetSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetSellerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.models.SellerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;

public class GetSellerActivityTest {
    @Mock
    private SellerDAO sellerDao;
    @Mock
    private ModelConverter modelConverter;

    private GetSellerActivity getSellerActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getSellerActivity = new GetSellerActivity(sellerDao, modelConverter);
    }

    @Test
    public void handleRequest_SellerFound() {
        // GIVEN
        String sellerId = "seller123";
        GetSellerRequest request = new GetSellerRequest(sellerId);

        Seller seller = createSampleSeller(sellerId);
        SellerModel expectedSellerModel = createSampleSellerModel(seller);

        Mockito.when(sellerDao.getSeller(sellerId)).thenReturn(seller);
        Mockito.when(modelConverter.toSellerModel(seller)).thenReturn(expectedSellerModel);

        // WHEN
        GetSellerResult result = getSellerActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Seller fetched successfully.", result.getMessage());
        assertEquals(expectedSellerModel, result.getSellerModel());
    }

    @Test
    public void handleRequest_SellerNotFound() {
        // GIVEN
        String sellerId = "nonexistentSeller";
        GetSellerRequest request = new GetSellerRequest(sellerId);

        Mockito.when(sellerDao.getSeller(sellerId)).thenReturn(null);

        // WHEN
        GetSellerResult result = getSellerActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Seller not found.", result.getMessage());
        assertNull(result.getSellerModel());
    }

    private Seller createSampleSeller(String sellerId) {
        Seller seller = new Seller();
        seller.setSellerID(sellerId);
        seller.setUsername("TestSeller");
        seller.setEmail("test@example.com");
        seller.setLocation("123 Main St");
        seller.setGarages(new HashSet<>());
        seller.setMessages(new HashSet<>());
        seller.setContactInfo("123-456-7890");
        seller.setSignupDate(LocalDateTime.now());
        return seller;
    }

    private SellerModel createSampleSellerModel(Seller seller) {
        return new SellerModel(
                seller.getSellerID(),
                seller.getUsername(),
                seller.getEmail(),
                seller.getLocation(),
                seller.getGarages(),
                seller.getMessages(),
                seller.getContactInfo(),
                seller.getSignupDate().toString()
        );
    }
}
