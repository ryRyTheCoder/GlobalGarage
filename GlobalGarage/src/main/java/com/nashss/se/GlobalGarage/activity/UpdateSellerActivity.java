package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.UpdateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.UpdateSellerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;
import com.nashss.se.GlobalGarage.models.SellerModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the UpdateSellerActivity.
 * Allows sellers to update their information.
 */
public class UpdateSellerActivity {
    private final Logger log = LogManager.getLogger();
    private final SellerDAO sellerDao;
    private final ModelConverter modelConverter;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new UpdateSellerActivity object.
     *
     * @param sellerDao SellerDAO to access and update seller data.
     * @param metricsPublisher MetricsPublisher to publish metrics.
     * @param modelConverter ModelConverter to convert entities.
     */
    @Inject
    public UpdateSellerActivity(SellerDAO sellerDao, ModelConverter modelConverter, MetricsPublisher metricsPublisher) {
        this.sellerDao = sellerDao;
        this.modelConverter = modelConverter;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Handles the incoming request by updating the seller's information.
     *
     * @param updateSellerRequest Request object containing the seller's updated details.
     * @return UpdateSellerResult Result object containing the updated {@link SellerModel}.
     */
    public UpdateSellerResult handleRequest(final UpdateSellerRequest updateSellerRequest) {
        log.info("Received UpdateSellerRequest {}", updateSellerRequest);

        Seller seller = sellerDao.getSeller(updateSellerRequest.getSellerID());

        // Update seller details
        updateSellerDetails(seller, updateSellerRequest);

        // Update seller in the database
        boolean updateSuccess = sellerDao.updateSeller(seller);

        // Handling the response based on the update success
        String message = updateSuccess ? "Seller updated successfully." : "Failed to update seller.";
        SellerModel sellerModel = updateSuccess ? modelConverter.toSellerModel(seller) : null;

        return UpdateSellerResult.builder()
                .withSuccess(updateSuccess)
                .withMessage(message)
                .withSeller(sellerModel)
                .build();
    }


    /**
     * Updates the details of a seller based on the provided request.
     *
     * @param seller  The seller to be updated.
     * @param request The update request containing the new details.
     */
    private void updateSellerDetails(Seller seller, UpdateSellerRequest request) {
        seller.setUsername(request.getUsername());
        seller.setEmail(request.getEmail());
        seller.setLocation(request.getLocation());
        seller.setGarages(request.getGarages());
        seller.setMessages(request.getMessages());
        seller.setContactInfo(request.getContactInfo());
    }
}
