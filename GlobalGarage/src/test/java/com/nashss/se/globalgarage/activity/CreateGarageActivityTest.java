package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.CreateGarageActivity;
import com.nashss.se.GlobalGarage.activity.request.CreateGarageRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateGarageResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.models.GarageModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        Mockito.when(garageDao.createGarage(any(Garage.class))).thenReturn(true);
        Mockito.when(modelConverter.toGarageModel(any(Garage.class))).thenReturn(dummyGarageModel);

        // WHEN
        CreateGarageResult result = createGarageActivity.handleRequest(request);

        // THEN
        assertTrue(result.isSuccess());
        assertNotNull(result.getGarageModel());

        // Capture the actual Garage object passed to garageDao.createGarage
        ArgumentCaptor<Garage> garageCaptor = ArgumentCaptor.forClass(Garage.class);
        Mockito.verify(garageDao).createGarage(garageCaptor.capture());
        Garage capturedGarage = garageCaptor.getValue();

        assertNotNull(capturedGarage.getGarageID());
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
        garage.setItems(new ArrayList<>());
        garage.setIsActive(true);
        return garage;
    }

    private GarageModel createSampleGarageModelFromGarage(Garage garage) {
        return new GarageModel(garage.getSellerID(), UUID.randomUUID().toString(),
                garage.getGarageName(), garage.getStartDate().toString(), garage.getEndDate().toString(),
                garage.getLocation(), garage.getDescription(), new ArrayList<>(), true);
    }

}
