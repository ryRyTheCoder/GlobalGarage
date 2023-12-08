package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.DeleteItemActivity;
import com.nashss.se.GlobalGarage.activity.request.DeleteItemRequest;
import com.nashss.se.GlobalGarage.activity.results.DeleteItemResult;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteItemActivityTest {
    @Mock
    private ItemDAO itemDao;
    @Mock
    private GarageDAO garageDao;

    private DeleteItemActivity deleteItemActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        deleteItemActivity = new DeleteItemActivity(itemDao, garageDao);
    }

    @Test
    public void handleRequest_ItemExistsAndDeleted_DeletesItem() {
        // GIVEN
        String itemId = "item123";
        String garageId = "garage123";
        String sellerId = "seller123";
        DeleteItemRequest request = new DeleteItemRequest(itemId, garageId, sellerId);
        Garage dummyGarage = createSampleGarageWithItem(itemId);

        // Stubbing the methods
        Mockito.when(itemDao.isItemExistsInGarage(itemId, garageId)).thenReturn(true);
        Mockito.when(itemDao.deleteItem(itemId, garageId)).thenReturn(true);
        Mockito.when(garageDao.getGarage(sellerId, garageId)).thenReturn(dummyGarage);
        Mockito.when(garageDao.updateGarage(any(Garage.class))).thenReturn(true);

        // WHEN
        DeleteItemResult result = deleteItemActivity.handleRequest(request);

        // THEN
        assertTrue(result.isSuccess());
        assertEquals("Item deleted successfully.", result.getMessage());
        verifyGarageUpdateWithoutItem(dummyGarage, itemId);
    }

    @Test
    public void handleRequest_ItemDoesNotExist_ReturnsFailure() {
        // GIVEN
        String itemId = "item123";
        String garageId = "garage123";
        String sellerId = "seller123";
        DeleteItemRequest request = new DeleteItemRequest(itemId, garageId, sellerId);

        // Stubbing the methods
        Mockito.when(itemDao.isItemExistsInGarage(itemId, garageId)).thenReturn(false);

        // WHEN
        DeleteItemResult result = deleteItemActivity.handleRequest(request);

        // THEN
        assertFalse(result.isSuccess());
        assertEquals("Item not found in the specified garage.", result.getMessage());
    }



    private Garage createSampleGarageWithItem(String itemId) {
        Garage garage = new Garage();
        garage.setSellerID("seller123");
        garage.setGarageID("garage123");
        garage.setGarageName("Sample Garage");
        garage.setItems(new ArrayList<>(Arrays.asList(itemId))); // Item is initially in the garage
        return garage;
    }

    private void verifyGarageUpdateWithoutItem(Garage expectedGarage, String removedItemId) {
        Mockito.verify(garageDao).updateGarage(argThat(garage ->
                garage.getGarageID().equals(expectedGarage.getGarageID()) &&
                        !garage.getItems().contains(removedItemId)
        ));
    }
}
