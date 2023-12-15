package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.UpdateBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.UpdateBuyerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import com.nashss.se.GlobalGarage.models.BuyerModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the UpdateBuyerActivity.
 * Allows buyers to update their information.
 */
public class UpdateBuyerActivity {
    private final Logger log = LogManager.getLogger();
    private final BuyerDAO buyerDao;
    private final ModelConverter modelConverter;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new UpdateBuyerActivity object.
     *
     * @param buyerDao BuyerDAO to access and update buyer data.
     * @param metricsPublisher MetricsPublisher to publish metrics.
     * @param modelConverter ModelConverter to convert entities.
     */
    @Inject
    public UpdateBuyerActivity(BuyerDAO buyerDao, ModelConverter modelConverter, MetricsPublisher metricsPublisher) {
        this.buyerDao = buyerDao;
        this.modelConverter = modelConverter;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Handles the incoming request by updating the buyer's information.
     *
     * @param updateBuyerRequest Request object containing the buyer's updated details.
     * @return UpdateBuyerResult Result object containing the updated {@link BuyerModel}.
     */
    public UpdateBuyerResult handleRequest(final UpdateBuyerRequest updateBuyerRequest) {
        log.info("Received UpdateBuyerRequest {}", updateBuyerRequest);

        Buyer buyer = buyerDao.getBuyer(updateBuyerRequest.getBuyerID());

        // Update buyer details
        updateBuyerDetails(buyer, updateBuyerRequest);

        // Update buyer in the database
        boolean updateSuccess = buyerDao.updateBuyer(buyer);

        // Handling the response based on the update success
        String message = updateSuccess ? "Buyer updated successfully." : "Failed to update buyer.";
        BuyerModel buyerModel = updateSuccess ? modelConverter.toBuyerModel(buyer) : null;

        return UpdateBuyerResult.builder()
                .withSuccess(updateSuccess)
                .withMessage(message)
                .withBuyer(buyerModel)
                .build();
    }

    /**
     * Updates the details of a buyer based on the provided request.
     *
     * @param buyer   The buyer to be updated.
     * @param request The update request containing the new details.
     */
    private void updateBuyerDetails(Buyer buyer, UpdateBuyerRequest request) {
        buyer.setUsername(request.getUsername());
        buyer.setEmail(request.getEmail());
        buyer.setLocation(request.getLocation());
    }
}
