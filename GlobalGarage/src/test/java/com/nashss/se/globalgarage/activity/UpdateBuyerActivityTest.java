package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.UpdateBuyerActivity;
import com.nashss.se.GlobalGarage.activity.request.UpdateBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.UpdateBuyerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import com.nashss.se.GlobalGarage.models.BuyerModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateBuyerActivityTest {

    @Mock
    private BuyerDAO buyerDao;

    @Mock
    private ModelConverter modelConverter;

    @Mock
    private MetricsPublisher metricsPublisher;

    private UpdateBuyerActivity updateBuyerActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        updateBuyerActivity = new UpdateBuyerActivity(buyerDao, modelConverter, metricsPublisher);
    }

    @Test
    void handleRequest_shouldUpdateBuyer() {
        // Arrange
        String buyerId = "buyer123";
        Set<String> updatedItemsInterested = new HashSet<>(Arrays.asList("item1", "item2"));
        Set<String> updatedMessages = new HashSet<>(Arrays.asList("message1", "message2"));
        String updatedContactInfo = "Updated Contact Info";
        String updatedSignupDate = "2023-01-01";

        UpdateBuyerRequest request = new UpdateBuyerRequest.Builder()
                .withBuyerID(buyerId)
                .withUsername("UpdatedUsername")
                .withEmail("updated@example.com")
                .withLocation("Updated Location")
                .build();

        Buyer existingBuyer = new Buyer();
        existingBuyer.setBuyerID(buyerId);
        existingBuyer.setUsername("CurrentUsername");
        existingBuyer.setEmail("current@example.com");
        existingBuyer.setLocation("Current Location");


        BuyerModel updatedModel = new BuyerModel(
                buyerId, "UpdatedUsername",
                "updated@example.com", "Updated Location",
                updatedItemsInterested, updatedMessages, updatedContactInfo
        );

        // Mock the behavior of DAO and Converter
        when(buyerDao.getBuyer(buyerId)).thenReturn(existingBuyer);
        when(buyerDao.updateBuyer(any(Buyer.class))).thenReturn(true);
        when(modelConverter.toBuyerModel(any(Buyer.class))).thenReturn(updatedModel);

        // Act
        UpdateBuyerResult result = updateBuyerActivity.handleRequest(request);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Buyer updated successfully.", result.getMessage());
        assertNotNull(result.getBuyer());
        assertEquals("UpdatedUsername", result.getBuyer().getUsername());
        assertEquals("updated@example.com", result.getBuyer().getEmail());
        assertEquals("Updated Location", result.getBuyer().getLocation());

        // Verify interactions with DAO and Converter
        verify(buyerDao).updateBuyer(existingBuyer);
        verify(modelConverter).toBuyerModel(existingBuyer);
    }
}
