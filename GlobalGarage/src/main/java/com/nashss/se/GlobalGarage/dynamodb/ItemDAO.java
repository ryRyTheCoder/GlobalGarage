package com.nashss.se.GlobalGarage.dynamodb;

import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.exceptions.ItemNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import java.util.List;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles database operations for items using {@link Item} to represent the model in DynamoDB.
 */

@Singleton
public class ItemDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();
    private Map<String, AttributeValue> lastEvaluatedKey = null;

    /**
     * Instantiates an ItemDAO object.
     *
     * @param dynamoDbMapper   the {@link DynamoDBMapper} used to interact with the items table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public ItemDAO(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.mapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Creates an item in the database.
     *
     * @param item the item object to be stored
     * @return true if the item is successfully created, false otherwise
     */
    public boolean createItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        try {
            mapper.save(item);
            metricsPublisher.addCount(MetricsConstants.CREATE_ITEM_SUCCESS_COUNT, 1);
            return true;
        } catch (Exception e) {
            log.error("Error creating item", e);
            metricsPublisher.addCount(MetricsConstants.CREATE_ITEM_FAIL_COUNT, 1);
            return false;
        }
    }

    /**
     * Retrieves an item by its garage ID and item ID.
     *
     * @param garageId the ID of the garage containing the item.
     * @param itemId   the ID of the item to retrieve.
     * @return        the {@link Item} object retrieved from the database.
     * @throws ItemNotFoundException if no item is found with the given IDs.
     */
    public Item getItem(String garageId, String itemId) {
        // Use DynamoDBMapper to load the item directly using its composite key
        Item item = this.mapper.load(Item.class, garageId, itemId);

        if (item == null) {
            metricsPublisher.addCount(MetricsConstants.ITEM_NOTFOUND_COUNT, 1);
            throw new ItemNotFoundException("Could not find item with garageID: " + garageId +
                    " and itemID: " + itemId);
        }
        metricsPublisher.addCount(MetricsConstants.ITEM_NOTFOUND_COUNT, 0);
        return item;
    }
    /**
     * Deletes an item from the database.
     *
     * @param garageId The ID of the garage from which the item is to be deleted.
     * @param itemId   The ID of the item to be deleted.
     * @return true if the deletion is successful, false otherwise.
     */
    public boolean deleteItem(String garageId, String itemId) {
        try {
            Item itemKey = new Item();
            itemKey.setGarageID(garageId);
            itemKey.setItemID(itemId);

            mapper.delete(itemKey);
            metricsPublisher.addCount(MetricsConstants.DELETE_ITEM_SUCCESS_COUNT, 1);
            log.info("Item deleted successfully: {}", itemId);
            return true;
        } catch (Exception e) {
            metricsPublisher.addCount(MetricsConstants.DELETE_ITEM_FAIL_COUNT, 1);
            log.error("Error deleting item: {}", itemId, e);
            return false;
        }
    }

    /**
     * Checks if an item exists in a specific garage.
     *
     * @param itemId   The ID of the item to check.
     * @param garageId The ID of the garage where the item might exist.
     * @return true if the item exists in the garage, false otherwise.
     */
    public boolean isItemExistsInGarage(String itemId, String garageId) {
        try {
            Item item = this.mapper.load(Item.class, garageId, itemId);
            return item != null;
        } catch (Exception e) {
            log.error("Error checking item existence: {}", itemId, e);
            return false;
        }
    }

    /**
     * Updates an existing item's information in the database.
     *
     * @param item  the {@link Item} object to be updated.
     * @return      true if the update is successful, false otherwise.
     */

    public boolean updateItem(Item item) {
        try {
            mapper.save(item);
            metricsPublisher.addCount(MetricsConstants.UPDATE_ITEM_SUCCESS_COUNT, 1);
            log.info("Item updated successfully: {}", item.getItemID());
            return true;
        } catch (Exception e) {
            metricsPublisher.addCount(MetricsConstants.UPDATE_ITEM_FAIL_COUNT, 1);
            log.error("Error updating item: {}", item.getItemID(), e);
            return false;
        }
    }
    /**
     * Retrieves a list of recent items, sorted by their listed date.
     * @param status Available status
     * @param limit            The maximum number of items to return.
     * @param encodedLastEvaluatedKey The last evaluated key for pagination, in Base64-encoded string format.
     * @return A pair of a list of recent items and the last evaluated key for pagination.
     */
    public Pair<List<Item>, Map<String, AttributeValue>> getRecentItems(String status, Integer limit,
                                                                        String encodedLastEvaluatedKey) {
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#status", "status");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":statusVal", new AttributeValue().withS(status));

        DynamoDBQueryExpression<Item> queryExpression = new DynamoDBQueryExpression<Item>()
                .withIndexName("StatusDateListedIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("#status = :statusVal")
                .withExpressionAttributeNames(expressionAttributeNames)
                .withExpressionAttributeValues(expressionAttributeValues)
                .withLimit(limit)
                .withScanIndexForward(false);

        if (encodedLastEvaluatedKey != null && !encodedLastEvaluatedKey.isEmpty()) {
            Map<String, AttributeValue> exclusiveStartKey = decodeLastEvaluatedKey(encodedLastEvaluatedKey);
            queryExpression.setExclusiveStartKey(exclusiveStartKey);
        }

        QueryResultPage<Item> queryResult = mapper.queryPage(Item.class, queryExpression);
        this.lastEvaluatedKey = queryResult.getLastEvaluatedKey();

        return Pair.of(queryResult.getResults(), queryResult.getLastEvaluatedKey());
    }

    /**
     * Decodes the last evaluated key from a Base64-encoded string to a Map.
     *
     * @param lastEvaluatedKeyBase64 The Base64-encoded last evaluated key.
     * @return A Map representing the last evaluated key.
     */
    private Map<String, AttributeValue> decodeLastEvaluatedKey(String lastEvaluatedKeyBase64) {
        byte[] bytes = Base64.getDecoder().decode(lastEvaluatedKeyBase64);
        try {
            return new ObjectMapper().readValue(bytes, new TypeReference<Map<String, AttributeValue>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Error decoding last evaluated key", e);
            throw new RuntimeException("Error decoding last evaluated key", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Gets the last evaluated key used in the last scan or query operation.
     * This key can be used for pagination in subsequent requests to continue where the last
     * query or scan left off. It's particularly useful in iterative data fetching operations.
     *
     * @return A map of AttributeValue representing the last evaluated key.
     */

    public Map<String, AttributeValue> getLastEvaluatedKey() {
        return this.lastEvaluatedKey;
    }

}
