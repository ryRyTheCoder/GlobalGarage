package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.CreateGarageActivity;
import com.nashss.se.GlobalGarage.activity.request.CreateGarageRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateGarageResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.models.GarageModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

public class CreateGarageActivityTest {
    @Mock
    private GarageDAO garageDao;
    @Mock
    private SellerDAO sellerDao;
    @Mock
    private ModelConverter modelConverter;

    private CreateGarageActivity createGarageActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createGarageActivity = new CreateGarageActivity(garageDao, sellerDao, modelConverter);
    }

    @Test
    public void handleRequest_validRequest_createsGarage() {
        // GIVEN
        CreateGarageRequest request = createSampleGarageRequest();
        Garage dummyGarage = createSampleGarageFromRequest(request);
        GarageModel dummyGarageModel = createSampleGarageModelFromGarage(dummyGarage);
        Seller dummySeller = createSampleSeller();

        // Stubbing the methods
        Mockito.when(garageDao.createGarage(any(Garage.class))).thenReturn(true);
        Mockito.when(sellerDao.getSeller(anyString())).thenReturn(dummySeller);
        Mockito.when(sellerDao.updateSeller(any(Seller.class))).thenReturn(true);
        Mockito.when(modelConverter.toGarageModel(any(Garage.class))).thenReturn(dummyGarageModel);

        // WHEN
        CreateGarageResult result = createGarageActivity.handleRequest(request);

        // THEN
        assertTrue(result.isSuccess());
        assertNotNull(result.getGarageModel());
        assertEquals("Garage created successfully.", result.getMessage());

        // Capture the actual Garage object passed to garageDao.createGarage
        ArgumentCaptor<Garage> garageCaptor = ArgumentCaptor.forClass(Garage.class);
        Mockito.verify(garageDao).createGarage(garageCaptor.capture());
        Garage capturedGarage = garageCaptor.getValue();

        // Assert other fields of the Garage object
        assertEquals(request.getSellerID(), capturedGarage.getSellerID());
        assertEquals(request.getGarageName(), capturedGarage.getGarageName());
        // ... other assertions for the Garage object

        // Verify updateSellerGarages behavior
        verifySellerUpdate(dummySeller, capturedGarage.getGarageID());
    }

// Additional helper methods...

    private void verifySellerUpdate(Seller expectedSeller, String expectedGarageID) {
        ArgumentCaptor<Seller> sellerArgumentCaptor = ArgumentCaptor.forClass(Seller.class);
        Mockito.verify(sellerDao).updateSeller(sellerArgumentCaptor.capture());

        Seller updatedSeller = sellerArgumentCaptor.getValue();
        assertTrue(updatedSeller.getGarages().contains(expectedGarageID));
        assertEquals(expectedSeller.getSellerID(), updatedSeller.getSellerID());
        // Additional assertions for seller details if necessary
    }

    private CreateGarageRequest createSampleGarageRequest() {
        String sellerID = "seller123";
        String garageName = "My Garage Sale";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);
        String location = "123 Main St";
        String description = "A variety of items for sale";

        return CreateGarageRequest.builder()
                .withSellerID(sellerID)
                .withGarageName(garageName)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withLocation(location)
                .withDescription(description)
                .build();
    }

    private Garage createSampleGarageFromRequest(CreateGarageRequest request) {
        Garage garage = new Garage();
        garage.setSellerID(request.getSellerID());
        garage.setGarageName(request.getGarageName());
        garage.setStartDate(request.getStartDate());
        garage.setEndDate(request.getEndDate());
        garage.setLocation(request.getLocation());
        garage.setDescription(request.getDescription());
        garage.setItems(new ArrayList<>()); // Items are set to empty as per the activity logic
        garage.setIsActive(true);
        return garage;
    }

    private GarageModel createSampleGarageModelFromGarage(Garage garage) {
        return new GarageModel(garage.getSellerID(), UUID.randomUUID().toString(),
                garage.getGarageName(), garage.getStartDate(), garage.getEndDate(),
                garage.getLocation(), garage.getDescription(), new ArrayList<>(), true);
    }

    private Seller createSampleSeller() {
        Seller seller = new Seller();
        seller.setSellerID("seller123");
        seller.setUsername("SampleUsername");
        seller.setEmail("sample@email.com");
        seller.setLocation("Sample Location");
        seller.setGarages(new HashSet<>()); // An empty set, as initially, the seller has no garages
        seller.setMessages(new HashSet<>()); // An empty set, assuming no initial messages
        seller.setContactInfo("123-456-7890");
        seller.setSignupDate(LocalDateTime.now()); // Set the current time as the signup date

        return seller;
    }
}
