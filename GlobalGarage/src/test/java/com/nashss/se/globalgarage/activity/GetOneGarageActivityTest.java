package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.GetOneGarageActivity;
import com.nashss.se.GlobalGarage.activity.request.GetOneGarageRequest;
import com.nashss.se.GlobalGarage.activity.results.GetOneGarageResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.models.GarageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

public class GetOneGarageActivityTest {
    @Mock
    private GarageDAO garageDao;
    @Mock
    private ModelConverter modelConverter;

    private GetOneGarageActivity getOneGarageActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getOneGarageActivity = new GetOneGarageActivity(garageDao, modelConverter);
    }

    @Test
    public void handleRequest_GarageExists_ReturnsGarage() {
        // GIVEN
        String sellerId = "seller123";
        String garageId = "garage123";
        GetOneGarageRequest request = new GetOneGarageRequest(sellerId, garageId);

        Garage dummyGarage = createSampleGarage(sellerId, garageId);
        GarageModel dummyGarageModel = createSampleGarageModel(dummyGarage);

        Mockito.when(garageDao.getGarage(sellerId, garageId)).thenReturn(dummyGarage);
        Mockito.when(modelConverter.toGarageModel(dummyGarage)).thenReturn(dummyGarageModel);

        // WHEN
        GetOneGarageResult result = getOneGarageActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Garage fetched successfully.", result.getMessage());
        assertNotNull(result.getGarageModel());
        assertEquals(dummyGarageModel, result.getGarageModel());
    }

    @Test
    public void handleRequest_GarageDoesNotExist_ReturnsError() {
        // GIVEN
        String sellerId = "seller123";
        String garageId = "garage123";
        GetOneGarageRequest request = new GetOneGarageRequest(sellerId, garageId);

        Mockito.when(garageDao.getGarage(sellerId, garageId)).thenReturn(null);

        // WHEN
        GetOneGarageResult result = getOneGarageActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Garage not found.", result.getMessage());
        assertNull(result.getGarageModel());
    }

    private Garage createSampleGarage(String sellerId, String garageId) {
        Garage garage = new Garage();
        garage.setSellerID(sellerId);
        garage.setGarageID(garageId);
        garage.setGarageName("Garage " + garageId);
        garage.setStartDate(LocalDateTime.now());
        garage.setEndDate(LocalDateTime.now().plusDays(1));
        garage.setLocation("123 Main St");
        garage.setDescription("Various items for sale");
        garage.setItems(Arrays.asList("item1", "item2"));
        garage.setIsActive(true);
        return garage;
    }

    private GarageModel createSampleGarageModel(Garage garage) {
        return new GarageModel(
                garage.getSellerID(),
                garage.getGarageID(),
                garage.getGarageName(),
                garage.getStartDate(),
                garage.getEndDate(),
                garage.getLocation(),
                garage.getDescription(),
                garage.getItems(),
                garage.getIsActive()
        );
    }
}
