package com.nashss.se.globalgarage.activity;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.GlobalGarage.activity.GetAllGaragesActivity;
import com.nashss.se.GlobalGarage.activity.request.GetAllGaragesRequest;
import com.nashss.se.GlobalGarage.activity.results.GetAllGaragesResult;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;


public class GetAllGaragesActivityTest {
    @Mock
    private GarageDAO garageDao;
    @Mock
    private ModelConverter modelConverter;


    private GetAllGaragesActivity getAllGaragesActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getAllGaragesActivity = new GetAllGaragesActivity(garageDao, modelConverter);
    }

    @Test
    public void handleRequest_ReturnsAllGarages() {
        // GIVEN
        Map<String, AttributeValue> decodedKeyMap = new HashMap<>();
        decodedKeyMap.put("dummyKey", new AttributeValue().withS("dummyValue"));

        // Create a valid JSON string and encode it to Base64
        String validJson = "{\"dummyKey\":\"dummyValue\"}";
        String encodedLastEvaluatedKey = Base64.getEncoder().encodeToString(validJson.getBytes(StandardCharsets.UTF_8));

        GetAllGaragesRequest request = new GetAllGaragesRequest(encodedLastEvaluatedKey);
        List<Garage> dummyGarages = createDummyGarages();
        Map<String, AttributeValue> newLastEvaluatedKey = new HashMap<>();

        Mockito.when(garageDao.getAllGarages(decodedKeyMap)).thenReturn(dummyGarages);
        Mockito.when(garageDao.getLastEvaluatedKey()).thenReturn(newLastEvaluatedKey);

        for (Garage garage : dummyGarages) {
            GarageModel garageModel = createSampleGarageModel(garage);
            Mockito.when(modelConverter.toGarageModel(garage)).thenReturn(garageModel);
        }



        // WHEN
        GetAllGaragesResult result = getAllGaragesActivity.handleRequest(request);

        // THEN
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(dummyGarages.size(), result.getGarageModels().size());
        assertEquals("Garages fetched successfully.", result.getMessage());
        assertNotNull(result.getLastEvaluatedKey());
    }

    private List<Garage> createDummyGarages() {
        List<Garage> garages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Garage garage = new Garage();
            garage.setSellerID("seller" + i);
            garage.setGarageID("garage" + i);
            garage.setGarageName("Garage " + i);
            garage.setStartDate(LocalDateTime.now());
            garage.setEndDate(LocalDateTime.now().plusDays(1));
            garage.setLocation("123 Main St " + i);
            garage.setDescription("Various items for sale at garage " + i);
            garage.setItems(Arrays.asList("item1", "item2"));
            garage.setIsActive(true);
            garages.add(garage);
        }
        return garages;
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
