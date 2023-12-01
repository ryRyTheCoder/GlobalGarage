package com.nashss.se.globalgarage.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.exceptions.ItemNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    void getItem_ItemExists_ReturnsItem() {
        String itemId = "item123";
        Item existingItem = createSampleItem();
        when(mockMapper.load(Item.class, itemId)).thenReturn(existingItem);
        Item result = itemDAO.getItem(itemId);
        assertEquals(existingItem, result);
    }

    @Test
    void getItem_ItemNotFound_ThrowsItemNotFoundException() {
        String itemId = "nonExistingItem";
        when(mockMapper.load(Item.class, itemId)).thenReturn(null);
        assertThrows(ItemNotFoundException.class, () -> itemDAO.getItem(itemId));
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
