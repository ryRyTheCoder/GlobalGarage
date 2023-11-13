package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.CreateItemRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateItemResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.models.ItemModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the CreateItemActivity for the GlobalGarage's CreateItem API.
 * This API allows a seller to add an item to a garage.
 */
public class CreateItemActivity {
    private final Logger log = LogManager.getLogger();
    private final ItemDAO itemDao;
    private final GarageDAO garageDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new CreateItemActivity object.
     *
     * @param itemDao to access the Item table.
     * @param garageDao to access the Garage table.
     * @param modelConverter to convert entities to models.
     */
    @Inject
    public CreateItemActivity(ItemDAO itemDao, GarageDAO garageDao, ModelConverter modelConverter) {
        this.itemDao = itemDao;
        this.garageDao = garageDao;
        this.modelConverter = modelConverter;
    }

    /**
     * This method handles the incoming request by creating a new item and adding it to a garage.
     *
     * @param createItemRequest request object to create an item
     * @return createItemResult result object indicating the outcome of the creation process
     */
    public CreateItemResult handleRequest(final CreateItemRequest createItemRequest) {
        log.info("Received CreateItemRequest {}", createItemRequest);

        String itemID = UUID.randomUUID().toString();
        String garageID = createItemRequest.getGarageID();
        String sellerID = createItemRequest.getSellerID();

        Item item = new Item();
        item.setGarageID(garageID);
        item.setItemID(itemID);
        item.setSellerID(sellerID);
        item.setName(createItemRequest.getName());
        item.setDescription(createItemRequest.getDescription());
        item.setPrice(createItemRequest.getPrice());
        item.setCategory(createItemRequest.getCategory());
        item.setImages(createItemRequest.getImages() != null ? new HashSet<>(createItemRequest.getImages()) : null);
        item.setDateListed(LocalDateTime.now());
        item.setBuyersInterested(new HashSet<>());
        item.setStatus("available");

        if (item.getBuyersInterested() != null && item.getBuyersInterested().isEmpty()) {
            item.setBuyersInterested(null);
        }
        if (item.getImages() != null && item.getImages().isEmpty()) {
            item.setImages(null);
        }

        boolean success = itemDao.createItem(item);

        if (success) {
            // Update the corresponding garage with the new item
            success = updateGarageItems(sellerID, garageID, itemID);
        }


        String message = success ? "Item created successfully." : "Failed to create item.";

        ItemModel itemModel = modelConverter.toItemModel(item);

        return CreateItemResult.builder()
                .withSuccess(success)
                .withMessage(message)
                .withItemModel(itemModel)
                .build();
    }

    private boolean updateGarageItems(String sellerID, String garageID, String itemID) {
        try {
            Garage garage = garageDao.getGarage(sellerID,garageID); // Load the garage
            List<String> items = garage.getItems();
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(itemID);
            garage.setItems(items);
            garageDao.updateGarage(garage); // Update the garage
            return true;
        } catch (Exception e) {
            log.error("Error updating garage items", e);
            return false;
        }
    }
}
