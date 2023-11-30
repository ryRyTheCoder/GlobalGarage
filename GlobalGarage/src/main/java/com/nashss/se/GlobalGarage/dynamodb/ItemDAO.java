package com.nashss.se.GlobalGarage.dynamodb;

import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.exceptions.ItemNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Retrieves an item by its ID.
     *
     * @param itemID  the ID of the item to retrieve.
     * @return        the {@link Item} object retrieved from the database.
     * @throws ItemNotFoundException if no item is found with the given ID.
     */

    public Item getItem(String itemID) {
        Item item = this.mapper.load(Item.class, itemID);

        if (item == null) {
            metricsPublisher.addCount(MetricsConstants.ITEM_NOTFOUND_COUNT, 1);
            throw new ItemNotFoundException("Could not find item with ID: " + itemID);
        }
        metricsPublisher.addCount(MetricsConstants.ITEM_NOTFOUND_COUNT, 0);
        return item;
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
}
