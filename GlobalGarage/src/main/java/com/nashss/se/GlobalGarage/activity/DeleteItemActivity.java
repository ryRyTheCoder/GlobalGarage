package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.DeleteItemRequest;
import com.nashss.se.GlobalGarage.activity.results.DeleteItemResult;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Implementation of the DeleteItemActivity for the GlobalGarage's DeleteItem API.
 * This API allows a seller to delete an item from a garage.
 */
public class DeleteItemActivity {
    private final Logger log = LogManager.getLogger();
    private final ItemDAO itemDao;
    private final GarageDAO garageDao;

    /**
     * Constructs a DeleteItemActivity instance with necessary dependencies.
     *
     * @param itemDao Data Access Object for Item operations.
     * @param garageDao Data Access Object for Garage operations.
     */

    @Inject
    public DeleteItemActivity(ItemDAO itemDao, GarageDAO garageDao) {
        this.itemDao = itemDao;
        this.garageDao = garageDao;
    }

    /**
     * Processes the request to delete an item from a garage.
     * Verifies the existence of the item in the specified garage before deletion.
     *
     * @param deleteItemRequest The request containing the details of the item to be deleted.
     * @return A result object indicating the success or failure of the delete operation.
     */

    public DeleteItemResult handleRequest(final DeleteItemRequest deleteItemRequest) {
        log.info("Received DeleteItemRequest {}", deleteItemRequest);

        String itemId = deleteItemRequest.getItemId();
        String garageId = deleteItemRequest.getGarageId();
        String sellerId = deleteItemRequest.getSellerId();

        // Check if item exists and is associated with the garage
        if (!itemDao.isItemExistsInGarage(itemId, garageId)) {
            return DeleteItemResult.builder()
                    .withSuccess(false)
                    .withMessage("Item not found in the specified garage.")
                    .build();
        }

        // Delete the item from the Item table
        boolean itemDeleteSuccess = itemDao.deleteItem(itemId, garageId);

        if (itemDeleteSuccess) {
            // Update the corresponding garage to remove the item from its list
            itemDeleteSuccess =  updateGarageItems(sellerId, garageId, itemId);
        }

        String message = itemDeleteSuccess ? "Item deleted successfully." : "Failed to delete item.";

        return DeleteItemResult.builder()
                .withSuccess(itemDeleteSuccess)
                .withMessage(message)
                .build();
    }

    /**
     * Updates the garage by removing the deleted item from its list of items.
     *
     * @param sellerId The ID of the seller associated with the garage.
     * @param garageId The ID of the garage from which the item is to be removed.
     * @param itemId The ID of the item that is being deleted.
     * @return True if the garage is successfully updated, false otherwise.
     */

    private boolean updateGarageItems(String sellerId, String garageId, String itemId) {
        try {
            Garage garage = garageDao.getGarage(sellerId, garageId);
            List<String> items = garage.getItems();
            items = items.stream().filter(i -> !i.equals(itemId)).collect(Collectors.toList());
            garage.setItems(items);
            garageDao.updateGarage(garage);
            return true;
        } catch (Exception e) {
            log.error("Error updating garage items after item deletion", e);
            return false;
        }
    }
}
