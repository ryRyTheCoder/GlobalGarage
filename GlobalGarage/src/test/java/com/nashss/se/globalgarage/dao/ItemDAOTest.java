package com.nashss.se.globalgarage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.exceptions.ItemNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ItemDAOTest {

    @Mock
    private DynamoDBMapper mockMapper;
    @Mock
    private MetricsPublisher mockMetricsPublisher;

    private ItemDAO itemDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemDAO = new ItemDAO(mockMapper, mockMetricsPublisher);
    }

    @Test
    void createItem_ValidItem_ReturnsTrue() {
        Item validItem = createSampleItem();
        doNothing().when(mockMapper).save(any(Item.class));
        assertTrue(itemDAO.createItem(validItem));
        verify(mockMetricsPublisher).addCount(MetricsConstants.CREATE_ITEM_SUCCESS_COUNT, 1);
    }

    @Test
    void createItem_NullItem_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> itemDAO.createItem(null));
    }


    @Test
    void getItem_ItemNotFound_ThrowsItemNotFoundException() {
        String garageId = "garage123";
        String itemId = "nonExistingItem";

        when(mockMapper.load(Item.class, garageId, itemId)).thenReturn(null);
        assertThrows(ItemNotFoundException.class, () -> itemDAO.getItem(garageId, itemId));
    }

    @Test
    void updateItem_ValidItem_ReturnsTrue() {
        Item validItem = createSampleItem();
        doNothing().when(mockMapper).save(any(Item.class));
        assertTrue(itemDAO.updateItem(validItem));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_ITEM_SUCCESS_COUNT, 1);
    }

    @Test
    void updateItem_FailedUpdate_ReturnsFalse() {
        Item validItem = createSampleItem();
        doThrow(new RuntimeException()).when(mockMapper).save(any(Item.class));
        assertFalse(itemDAO.updateItem(validItem));
        verify(mockMetricsPublisher).addCount(MetricsConstants.UPDATE_ITEM_FAIL_COUNT, 1);
    }
    @Test
    void deleteItem_ValidItem_ReturnsTrue() {
        String garageId = "garage123";
        String itemId = "item123";
        doNothing().when(mockMapper).delete(any(Item.class));
        assertTrue(itemDAO.deleteItem(garageId, itemId));
        verify(mockMetricsPublisher).addCount(MetricsConstants.DELETE_ITEM_SUCCESS_COUNT, 1);
    }

    @Test
    void deleteItem_FailedDeletion_ReturnsFalse() {
        String garageId = "garage123";
        String itemId = "item123";
        doThrow(new RuntimeException()).when(mockMapper).delete(any(Item.class));
        assertFalse(itemDAO.deleteItem(garageId, itemId));
        verify(mockMetricsPublisher).addCount(MetricsConstants.DELETE_ITEM_FAIL_COUNT, 1);
    }
    @Test
    void isItemExistsInGarage_ItemExists_ReturnsTrue() {
        String garageId = "garage123";
        String itemId = "item123";
        Item sampleItem = createSampleItem();
        when(mockMapper.load(Item.class, garageId, itemId)).thenReturn(sampleItem);
        assertTrue(itemDAO.isItemExistsInGarage(itemId, garageId));
    }

    @Test
    void isItemExistsInGarage_ItemNotExists_ReturnsFalse() {
        String garageId = "garage123";
        String itemId = "item123";
        when(mockMapper.load(Item.class, garageId, itemId)).thenReturn(null);
        assertFalse(itemDAO.isItemExistsInGarage(itemId, garageId));
    }

    @Test
    void isItemExistsInGarage_ErrorOccurs_ReturnsFalse() {
        String garageId = "garage123";
        String itemId = "item123";
        doThrow(new RuntimeException()).when(mockMapper).load(Item.class, garageId, itemId);
        assertFalse(itemDAO.isItemExistsInGarage(itemId, garageId));
    }
    @Test
    void getRecentItems_WithValidParameters_ReturnsItems() {
        // Given
        String status = "available";
        Integer limit = 10;
        String encodedLastEvaluatedKey = null;

        // Mocking DynamoDBMapper behavior
        List<Item> expectedItems = Arrays.asList(createSampleItem());
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("dateListed", new AttributeValue().withS("2023-11-13T23:33:02.444077"));

        QueryResultPage<Item> queryResultPage = new QueryResultPage<>();
        queryResultPage.setResults(expectedItems);
        queryResultPage.setLastEvaluatedKey(lastEvaluatedKey);

        when(mockMapper.queryPage(eq(Item.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(queryResultPage);

        // When
        Pair<List<Item>, Map<String, AttributeValue>> result = itemDAO.getRecentItems(status, limit, encodedLastEvaluatedKey);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedItems, result.getLeft());
        assertNotNull(result.getRight());
        assertEquals(lastEvaluatedKey, result.getRight());
    }
    @Test
    void getLastEvaluatedKey_AfterQuery_ReturnsLastKey() {
        // Given
        String status = "available";
        Integer limit = 10;
        String encodedLastEvaluatedKey = null; // Simulate no starting key

        // Mocking DynamoDBMapper behavior
        List<Item> expectedItems = Arrays.asList(createSampleItem());
        Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
        lastEvaluatedKey.put("dateListed", new AttributeValue().withS("2023-11-13T23:33:02.444077"));

        QueryResultPage<Item> queryResultPage = new QueryResultPage<>();
        queryResultPage.setResults(expectedItems);
        queryResultPage.setLastEvaluatedKey(lastEvaluatedKey);

        when(mockMapper.queryPage(eq(Item.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(queryResultPage);

        // When
        itemDAO.getRecentItems(status, limit, encodedLastEvaluatedKey);

        // Then
        Map<String, AttributeValue> actualLastEvaluatedKey = itemDAO.getLastEvaluatedKey();
        assertNotNull(actualLastEvaluatedKey);
        assertEquals(lastEvaluatedKey, actualLastEvaluatedKey);
    }

    private Item createSampleItem() {
        Item item = new Item();
        item.setItemID("item123");
        item.setGarageID("garage123");
        item.setSellerID("seller123");
        item.setName("Sample Item");
        item.setDescription("Description of Sample Item");
        item.setPrice(new BigDecimal("99.99"));
        item.setCategory("Electronics");
        Set<String> images = new HashSet<>();
        images.add("https://example.com/image1.jpg");
        images.add("https://example.com/image2.jpg");
        item.setImages(images);
        item.setDateListed(LocalDateTime.now());
        Set<String> buyersInterested = new HashSet<>();
        buyersInterested.add("buyer1");
        buyersInterested.add("buyer2");
        item.setBuyersInterested(buyersInterested);
        item.setStatus("Available");
        return item;
    }
}
