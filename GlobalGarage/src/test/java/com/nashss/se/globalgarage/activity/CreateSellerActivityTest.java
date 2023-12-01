package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.CreateSellerActivity;
import com.nashss.se.GlobalGarage.activity.request.CreateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateSellerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.models.SellerModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

public class CreateSellerActivityTest {
    @Mock
    private SellerDAO sellerDao;
    @Mock
    private ModelConverter modelConverter;

    private CreateSellerActivity createSellerActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createSellerActivity = new CreateSellerActivity(sellerDao, modelConverter);
    }

    @Test
    public void handleRequest_validRequest_createsSeller() {
        // GIVEN
        CreateSellerRequest request = createSampleSellerRequest();
        assertNotNull(request, "CreateSellerRequest is null");

        Seller dummySeller = createSampleSellerFromRequest(request);
        assertNotNull(dummySeller, "Dummy Seller is null");

        SellerModel dummySellerModel = createSampleSellerModelFromSeller(dummySeller);
        assertNotNull(dummySellerModel, "Dummy SellerModel is null");

        // Configure mock behavior
        Mockito.when(sellerDao.createSeller(any(Seller.class))).thenReturn(true);
        Mockito.when(modelConverter.toSellerModel(any(Seller.class))).thenReturn(dummySellerModel);

        // WHEN
        CreateSellerResult result = createSellerActivity.handleRequest(request);
        assertNotNull(result, "CreateSellerResult is null");

        // THEN
        assertTrue(result.isSuccess());
        assertNotNull(result.getSellerModel());
        assertEquals("Seller created successfully.", result.getMessage());

        // Assertions for the Seller object
        ArgumentCaptor<Seller> sellerCaptor = ArgumentCaptor.forClass(Seller.class);
        Mockito.verify(sellerDao).createSeller(sellerCaptor.capture());
        Seller capturedSeller = sellerCaptor.getValue();

        assertEquals("S" + request.getSellerId(), capturedSeller.getSellerID());
        assertEquals(request.getUsername(), capturedSeller.getUsername());
        assertEquals(request.getEmail(), capturedSeller.getEmail());
        assertEquals(request.getLocation(), capturedSeller.getLocation());
        assertEquals(request.getContactInfo(), capturedSeller.getContactInfo());

    }

    // Additional helper methods...

    private CreateSellerRequest createSampleSellerRequest() {
        return new CreateSellerRequest(
                "seller123",
                "SampleUsername",
                "sample@example.com",
                "123 Main St",
                "123-456-7890"
        );
    }


    private Seller createSampleSellerFromRequest(CreateSellerRequest request) {
        Seller seller = new Seller();
        seller.setSellerID("S" + request.getSellerId());
        seller.setUsername(request.getUsername());
        seller.setEmail(request.getEmail());
        seller.setLocation(request.getLocation());
        seller.setContactInfo(request.getContactInfo());
        seller.setGarages(new HashSet<>()); // Initially, the seller has no garages
        seller.setMessages(new HashSet<>()); // Initially, the seller has no messages
        seller.setSignupDate(LocalDateTime.now()); // Set the current time as the signup date
        return seller;
    }

    private SellerModel createSampleSellerModelFromSeller(Seller seller) {
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
