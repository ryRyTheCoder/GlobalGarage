package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.GetGaragesBySellerActivity;
import com.nashss.se.GlobalGarage.activity.request.GetGaragesBySellerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetGaragesBySellerResult;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GetGaragesBySellerActivityTest {
    @Mock
    private GarageDAO garageDao;
    @Mock
    private ModelConverter modelConverter;

    private GetGaragesBySellerActivity getGaragesBySellerActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getGaragesBySellerActivity = new GetGaragesBySellerActivity(garageDao, modelConverter);
    }

    @Test
    public void handleRequest_FetchesGaragesSuccessfully() {
        // GIVEN
        String sellerId = "seller123";
        Integer limit = 5;
        String lastEvaluatedKey = "someKey";
        GetGaragesBySellerRequest request = new GetGaragesBySellerRequest(sellerId, limit, lastEvaluatedKey);
        List<Garage> garages = createDummyGaragesForSeller(sellerId, limit);
        List<GarageModel> garageModels = garages.stream()
                .map(this::createSampleGarageModel)
                .collect(Collectors.toList());

        Mockito.when(garageDao.getGaragesBySeller(sellerId, limit, lastEvaluatedKey))
                .thenReturn(garages);
        Mockito.when(modelConverter.toGarageModel(Mockito.any(Garage.class)))
                .thenAnswer(invocation -> createSampleGarageModel(invocation.getArgument(0)));

        // WHEN
        GetGaragesBySellerResult result = getGaragesBySellerActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Garages fetched successfully.", result.getMessage());
        assertEquals(garageModels, result.getGarageModels());
    }

    @Test
    public void handleRequest_NoGaragesExist_ReturnsEmptyResult() {
        // GIVEN
        String sellerId = "seller123";
        GetGaragesBySellerRequest request = new GetGaragesBySellerRequest(sellerId, null, null);

        Mockito.when(garageDao.getGaragesBySeller(sellerId, null, null)).thenReturn(Arrays.asList());

        // WHEN
        GetGaragesBySellerResult result = getGaragesBySellerActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Garages fetched successfully.", result.getMessage());
        assertTrue(result.getGarageModels().isEmpty());
    }

    // Helper methods to create dummy objects
    private List<Garage> createDummyGaragesForSeller(String sellerId, int count) {
        return IntStream.range(0, count).mapToObj(i -> {
            Garage garage = new Garage();
            garage.setSellerID(sellerId);
            garage.setGarageID("garage" + i);
            garage.setGarageName("Garage " + i);
            garage.setStartDate(LocalDateTime.now());
            garage.setEndDate(LocalDateTime.now().plusDays(1));
            garage.setLocation("123 Main St " + i);
            garage.setDescription("Various items for sale at garage " + i);
            garage.setItems(Arrays.asList("item1", "item2"));
            garage.setIsActive(true);
            return garage;
        }).collect(Collectors.toList());
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