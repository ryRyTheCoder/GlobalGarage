package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.CreateItemActivity;
import com.nashss.se.GlobalGarage.activity.request.CreateItemRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateItemResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.models.ItemModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

public class CreateItemActivityTest {
    @Mock
    private ItemDAO itemDao;
    @Mock
    private GarageDAO garageDao;
    @Mock
    private ModelConverter modelConverter;

    private CreateItemActivity createItemActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createItemActivity = new CreateItemActivity(itemDao, garageDao, modelConverter);
    }

    @Test
    public void handleRequest_validRequest_createsItem() {
        // GIVEN
        CreateItemRequest request = createSampleItemRequest();
        Item dummyItem = createSampleItemFromRequest(request);
        ItemModel dummyItemModel = createSampleItemModelFromItem(dummyItem);
        Garage dummyGarage = createSampleGarage();

        // Stubbing the methods
        Mockito.when(itemDao.createItem(any(Item.class))).thenReturn(true);
        Mockito.when(garageDao.getGarage(anyString(), anyString())).thenReturn(dummyGarage);
        Mockito.when(garageDao.updateGarage(any(Garage.class))).thenReturn(true);
        Mockito.when(modelConverter.toItemModel(any(Item.class))).thenReturn(dummyItemModel);

        // WHEN
        CreateItemResult result = createItemActivity.handleRequest(request);

        // THEN
        assertTrue(result.isSuccess());
        assertNotNull(result.getItemModel());
        assertEquals("Item created successfully.", result.getMessage());

        // Capturing and asserting the Item object
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(itemDao).createItem(itemCaptor.capture());
        Item capturedItem = itemCaptor.getValue();
        // ... Assertions for the Item object

        // Verifying garage update behavior
        verifyGarageUpdate(dummyGarage, capturedItem.getItemID());
    }

    // Additional helper methods and verifications...

    private CreateItemRequest createSampleItemRequest() {
        return new CreateItemRequest(
                "seller123",
                "garage123",
                "Sample Item",
                "This is a sample item description",
                new BigDecimal("19.99"),
                "Sample Category",
                new HashSet<>(Arrays.asList("image1.jpg", "image2.jpg"))
        );
    }

    private Item createSampleItemFromRequest(CreateItemRequest request) {
        Item item = new Item();
        item.setGarageID(request.getGarageID());
        item.setItemID(UUID.randomUUID().toString());
        item.setSellerID(request.getSellerID());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        item.setImages(request.getImages());
        item.setDateListed(LocalDateTime.now());
        item.setBuyersInterested(new HashSet<>());
        item.setStatus("available");
        return item;
    }

    private ItemModel createSampleItemModelFromItem(Item item) {
        return new ItemModel(
                item.getGarageID(),
                item.getItemID(),
                item.getSellerID(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory(),
                item.getImages(),
                item.getDateListed().toString(),
                item.getBuyersInterested(),
                item.getStatus()
        );
    }

    private Garage createSampleGarage() {
        Garage garage = new Garage();
        garage.setSellerID("seller123");
        garage.setGarageID("garage123");
        garage.setGarageName("Sample Garage");
        garage.setStartDate(LocalDateTime.now());
        garage.setEndDate(LocalDateTime.now().plusDays(1));
        garage.setLocation("123 Main St");
        garage.setDescription("Sample garage description");
        garage.setItems(new ArrayList<>());
        garage.setIsActive(true);
        return garage;
    }

    private void verifyGarageUpdate(Garage expectedGarage, String expectedItemID) {
        ArgumentCaptor<Garage> garageArgumentCaptor = ArgumentCaptor.forClass(Garage.class);
        Mockito.verify(garageDao).updateGarage(garageArgumentCaptor.capture());

        Garage updatedGarage = garageArgumentCaptor.getValue();
        assertEquals(expectedGarage.getGarageID(), updatedGarage.getGarageID());
        assertTrue(updatedGarage.getItems().contains(expectedItemID));
    }
}
