package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.GetBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetBuyerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.models.BuyerModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetBuyerActivity {
    private final Logger log = LogManager.getLogger();
    private final BuyerDAO buyerDao;
    private final ModelConverter modelConverter;

    /**
     * Constructs a GetBuyerActivity instance.
     *
     * @param buyerDao      The {@link BuyerDAO} used for accessing and manipulating buyer data.
     * @param modelConverter The {@link ModelConverter} used for converting {@link Buyer} objects
     *                      to {@link BuyerModel} objects.
     */
    @Inject
    public GetBuyerActivity(BuyerDAO buyerDao, ModelConverter modelConverter) {
        this.buyerDao = buyerDao;
        this.modelConverter = modelConverter;
    }

    /**
     * Handles the request to retrieve a specific buyer by their ID.
     * This method fetches the buyer details from the database using {@link BuyerDAO},
     * checks if the buyer exists, and then converts the {@link Buyer} entity to a {@link BuyerModel}.
     *
     * @param request The {@link GetBuyerRequest} containing the ID of the buyer to be retrieved.
     * @return        A {@link GetBuyerResult} object representing the outcome of the operation,
     *                including success status, a message, and the fetched buyer model.
     */
    public GetBuyerResult handleRequest(final GetBuyerRequest request) {
        log.info("Received GetBuyerRequest with buyerId: {}", request.getBuyerId());

        // Fetch buyer from the database
        Buyer buyer = buyerDao.getBuyer(request.getBuyerId());

        if (buyer == null) {
            log.error("Buyer not found with id: {}", request.getBuyerId());
            return GetBuyerResult.builder()
                    .withSuccess(false)
                    .withMessage("Buyer not found.")
                    .build();
        }

        // Convert buyer to buyer model
        BuyerModel buyerModel = modelConverter.toBuyerModel(buyer);

        return GetBuyerResult.builder()
                .withSuccess(true)
                .withMessage("Buyer fetched successfully.")
                .withBuyerModel(buyerModel)
                .build();
    }
}
