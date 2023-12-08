package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.GetItemRequest;
import com.nashss.se.GlobalGarage.activity.results.GetItemResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;
import com.nashss.se.GlobalGarage.models.ItemModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetItemActivity {
    private final Logger log = LogManager.getLogger();
    private final ItemDAO itemDao;
    private final ModelConverter modelConverter;

    /**
     * Constructs a GetItemActivity instance.
     *
     * @param itemDao        The {@link ItemDAO} used for accessing and manipulating item data.
     * @param modelConverter The {@link ModelConverter} used for converting between different model types.
     */
    @Inject
    public GetItemActivity(ItemDAO itemDao, ModelConverter modelConverter) {
        this.itemDao = itemDao;
        this.modelConverter = modelConverter;
    }

    /**
     * Handles the request to retrieve a specific item by its ID.
     * This method fetches the item details from the database using {@link ItemDAO},
     * converts the {@link Item} entity to a {@link ItemModel}, and returns the result.
     *
     * @param request The request object containing the item ID.
     * @return        A {@link GetItemResult} object representing the outcome of the operation,
     *                including success status, a message, and the fetched item model.
     */
    public GetItemResult handleRequest(final GetItemRequest request) {
        log.info("Received GetItemRequest with itemId: {} and garageId: {}",
                request.getItemId(), request.getGarageId());

        // Fetch item from the database
        Item item = itemDao.getItem(request.getGarageId(), request.getItemId());

        if (item == null) {
            log.error("Item not found with id: {} in garage: {}", request.getItemId(), request.getGarageId());
            return GetItemResult.builder()
                    .withSuccess(false)
                    .withMessage("Item not found.")
                    .build();
        }
        log.info("Retrieved Item from database: {}", item);
        // Convert item to item model
        ItemModel itemModel = modelConverter.toItemModel(item);
        // Log the ItemModel object after conversion
        log.info("Converted ItemModel: {}", itemModel);
        return GetItemResult.builder()
                .withSuccess(true)
                .withMessage("Item fetched successfully.")
                .withItemModel(itemModel)
                .build();
    }
}
