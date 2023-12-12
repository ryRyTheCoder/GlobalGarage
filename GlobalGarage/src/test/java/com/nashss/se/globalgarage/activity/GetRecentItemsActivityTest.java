package com.nashss.se.globalgarage.activity;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.GlobalGarage.activity.GetRecentItemsActivity;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nashss.se.GlobalGarage.activity.request.GetRecentItemsRequest;
import com.nashss.se.GlobalGarage.activity.results.GetRecentItemsResult;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.models.ItemModel;
import com.nashss.se.GlobalGarage.converters.ModelConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;


class GetRecentItemsActivityTest {

    @Mock
    private ItemDAO mockItemDAO;
    @Mock
    private ModelConverter mockModelConverter;

    private GetRecentItemsActivity activity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activity = new GetRecentItemsActivity(mockItemDAO, mockModelConverter);
    }

//    @Test
//    void handleRequest_ValidRequest_ReturnsRecentItems() {
//        // Given
//        GetRecentItemsRequest request = new GetRecentItemsRequest(10, null); // Example request
//
//        // Mock the DAO and model converter behavior
//        List<Item> daoResponseItems = Arrays.asList(createSampleItem());
//        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
//        String encodedLastEvaluatedKey = "encodedKey"; // Mocked encoded key
//
//        when(mockItemDAO.getRecentItems(eq("available"), anyInt(), anyString())).thenReturn(Pair.of(daoResponseItems, lastEvaluatedKey));
//        when(mockModelConverter.toItemModel(any(Item.class))).thenReturn(createSampleItemModel());
//
//        // Encoding the last evaluated key manually for the test
//        String encodedKeyForTest = null;
//        try {
//            encodedKeyForTest = Base64.getEncoder().encodeToString(
//                    new ObjectMapper().writeValueAsBytes(lastEvaluatedKey));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        // When
//        GetRecentItemsResult result = activity.handleRequest(request);
//
//        // Then
//        assertTrue(result.isSuccess());
//        assertEquals("Recent available items fetched successfully.", result.getMessage());
//        assertNotNull(result.getItemModels());
//        assertEquals(1, result.getItemModels().size());
//        assertEquals(encodedKeyForTest, result.getLastEvaluatedKey()); // Checking encoded last evaluated key
//    }

    private Item createSampleItem() {
        Item item = new Item();
        item.setGarageID("garage123");
        item.setItemID("item123");
        item.setSellerID("seller123");
        item.setName("Sample Item");
        item.setDescription("A sample description for the item");
        item.setPrice(new BigDecimal("29.99"));
        item.setCategory("Electronics");
        item.setImages(new HashSet<>(Arrays.asList("https://example.com/image1.jpg", "https://example.com/image2.jpg")));
        item.setDateListed(LocalDateTime.now());
        item.setBuyersInterested(new HashSet<>(Arrays.asList("buyer1", "buyer2")));
        item.setStatus("Available");
        return item;
    }

    private ItemModel createSampleItemModel() {
        ItemModel model = new ItemModel(
                "garage123",
                "item123",
                "seller123",
                "Sample Item",
                "A sample description for the item",
                new BigDecimal("29.99"),
                "Electronics",
                new HashSet<>(Arrays.asList("https://example.com/image1.jpg", "https://example.com/image2.jpg")),
                LocalDateTime.now().toString(),
                new HashSet<>(Arrays.asList("buyer1", "buyer2")),
                "Available"
        );
        return model;
    }
}
