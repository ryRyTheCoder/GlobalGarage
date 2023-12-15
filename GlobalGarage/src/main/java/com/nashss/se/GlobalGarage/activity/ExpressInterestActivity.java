package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.ExpressInterestRequest;
import com.nashss.se.GlobalGarage.activity.results.ExpressInterestResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.ItemDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.dynamodb.models.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

import javax.inject.Inject;

/**
 * Implementation of the ExpressInterestActivity for the GlobalGarage's ExpressInterest API.
 * This API allows a buyer to express interest in an item.
 */
public class ExpressInterestActivity {
    private final Logger log = LogManager.getLogger();
    private final ItemDAO itemDao;
    private final BuyerDAO buyerDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new ExpressInterestActivity object.
     *
     * @param itemDao ItemDAO to access the Item table.
     * @param buyerDao BuyerDAO to access the Buyer table.
     * @param modelConverter to convert entities to models.
     */
    @Inject
    public ExpressInterestActivity(ItemDAO itemDao, BuyerDAO buyerDao, ModelConverter modelConverter) {
        this.itemDao = itemDao;
        this.buyerDao = buyerDao;
        this.modelConverter = modelConverter;

    }

    /**
     * Handles the incoming request by expressing interest in an item.
     *
     * @param expressInterestRequest request object to express interest
     * @return expressInterestResult result object indicating the outcome of the process
     */
    public ExpressInterestResult handleRequest(final ExpressInterestRequest expressInterestRequest) {
        log.info("Received ExpressInterestRequest {}", expressInterestRequest);

        // Update Item to include Buyer's Interest
        boolean success = updateItemBuyersInterested(expressInterestRequest);

        // Update Buyer to include Item's Interest
        if (success) {
            success = updateBuyerItemsInterested(expressInterestRequest);
        }

        String message = success ? "Interest expressed successfully." : "Failed to express interest.";

        return ExpressInterestResult.builder()
                .withSuccess(success)
                .withMessage(message)
                .build();
    }

    private boolean updateItemBuyersInterested(ExpressInterestRequest request) {
        try {
            Item item = itemDao.getItem(request.getGarageID(), request.getItemID());
            if (item.getBuyersInterested() == null) {
                item.setBuyersInterested(new HashSet<>());
            }
            item.getBuyersInterested().add(request.getBuyerID());
            itemDao.updateItem(item);
            return true;
        } catch (Exception e) {
            log.error("Error updating item buyers interested", e);
            return false;
        }
    }

    private boolean updateBuyerItemsInterested(ExpressInterestRequest request) {
        try {
            Buyer buyer = buyerDao.getBuyer(request.getBuyerID());
            if (buyer.getItemsInterested() == null) {
                buyer.setItemsInterested(new HashSet<>());
            }
            String concatenatedId = request.getItemID() + ":" + request.getGarageID();
            buyer.getItemsInterested().add(concatenatedId);
            buyerDao.updateBuyer(buyer);
            return true;
        } catch (Exception e) {
            log.error("Error updating buyer items interested", e);
            return false;
        }
    }
}
