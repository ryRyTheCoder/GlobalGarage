package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.UpdateSellerActivity;
import com.nashss.se.GlobalGarage.activity.request.UpdateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.UpdateSellerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import com.nashss.se.GlobalGarage.models.SellerModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateSellerActivityTest {

    @Mock
    private SellerDAO sellerDao;

    @Mock
    private ModelConverter modelConverter;

    @Mock
    private MetricsPublisher metricsPublisher;

    private UpdateSellerActivity updateSellerActivity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        updateSellerActivity = new UpdateSellerActivity(sellerDao, modelConverter, metricsPublisher);
    }

    @Test
    void handleRequest_shouldUpdateSeller() {
        // Arrange
        String sellerId = "seller123";
        UpdateSellerRequest request = new UpdateSellerRequest.Builder()
                .withSellerID(sellerId)
                .withUsername("UpdatedUsername")
                .withEmail("updated@example.com")
                .withLocation("Updated Location")
                .withContactInfo("Updated Contact Info")
                .build();
        Seller existingSeller = new Seller();
        existingSeller.setSellerID(sellerId);
        SellerModel updatedModel = new SellerModel(sellerId, "UpdatedUsername",
                "updated@example.com", "Updated Location",
                new HashSet<>(), new HashSet<>(),
                "Updated Contact Info", "2023-01-01");

        when(sellerDao.getSeller(sellerId)).thenReturn(existingSeller);
        when(sellerDao.updateSeller(existingSeller)).thenReturn(true);
        when(modelConverter.toSellerModel(existingSeller)).thenReturn(updatedModel);

        // Act
        UpdateSellerResult result = updateSellerActivity.handleRequest(request);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Seller updated successfully.", result.getMessage());
        assertNotNull(result.getSeller());
        verify(sellerDao).updateSeller(existingSeller);
        verify(modelConverter).toSellerModel(existingSeller);
    }
}
