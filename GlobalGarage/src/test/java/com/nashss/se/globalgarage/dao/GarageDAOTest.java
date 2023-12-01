package com.nashss.se.globalgarage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.exceptions.GarageNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class GarageDAOTest {

    @InjectMocks
    private GarageDAO garageDAO;
    @Mock
    private DynamoDBMapper mockMapper;
    @Mock
    private MetricsPublisher mockMetricsPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createGarage_ValidGarage_ReturnsTrue() {
        Garage validGarage = createSampleGarage();
        doNothing().when(mockMapper).save(any(Garage.class));
        assertTrue(garageDAO.createGarage(validGarage));
        verify(mockMetricsPublisher).addCount(MetricsConstants.CREATE_GARAGE_SUCCESS_COUNT, 1);
    }

    @Test
    void createGarage_NullGarage_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> garageDAO.createGarage(null));
    }



    @Test
    void getGarage_GarageNotFound1_ThrowsGarageNotFoundException() {
        String sellerId = "seller123";
        String garageId = "garage123";

        when(mockMapper.load(Garage.class, sellerId, garageId)).thenReturn(null);

        Exception exception = assertThrows(GarageNotFoundException.class, () -> garageDAO.getGarage(sellerId, garageId));

    }

    @Test
    void getGarage_GarageNotFound_ThrowsGarageNotFoundException() {
        // GIVEN
        when(mockMapper.load(eq(Garage.class), any(), any())).thenReturn(null);

        // WHEN + THEN
        assertThrows(GarageNotFoundException.class, () -> garageDAO.getGarage("nonexistentSeller", "nonexistentGarage"));
    }

    @Test
    void updateGarage_ValidGarage_ReturnsTrue() {
        Garage validGarage = createSampleGarage();
        doNothing().when(mockMapper).save(any(Garage.class));
        assertTrue(garageDAO.updateGarage(validGarage));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_GARAGE_SUCCESS_COUNT, 1);
    }

    @Test
    void updateGarage_FailedUpdate_ReturnsFalse() {
        Garage validGarage = createSampleGarage();
        doThrow(new RuntimeException()).when(mockMapper).save(any(Garage.class));
        assertFalse(garageDAO.updateGarage(validGarage));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_GARAGE_FAIL_COUNT, 1);
    }

    private Garage createSampleGarage() {
        Garage garage = new Garage();
        garage.setSellerID("seller123");
        garage.setGarageID("garage123");
        garage.setGarageName("Awesome Garage Sale");
        garage.setStartDate(LocalDateTime.now());
        garage.setEndDate(LocalDateTime.now().plusDays(2));
        garage.setLocation("123 Main Street");
        garage.setDescription("A variety of items available");
        garage.setItems(Arrays.asList("item1", "item2"));
        garage.setIsActive(true);
        return garage;
    }
}
