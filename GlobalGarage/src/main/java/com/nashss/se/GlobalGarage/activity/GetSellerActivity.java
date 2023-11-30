package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.GetSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetSellerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.models.SellerModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetSellerActivity {
    private final Logger log = LogManager.getLogger();
    private final SellerDAO sellerDao;
    private final ModelConverter modelConverter;

    /**
     * Constructs a GetSellerActivity instance.
     *
     * @param sellerDao      The {@link SellerDAO} used for accessing and manipulating seller data.
     * @param modelConverter The {@link ModelConverter} used for converting {@link Seller} objects
     *                      to {@link SellerModel} objects.
     */

    @Inject
    public GetSellerActivity(SellerDAO sellerDao, ModelConverter modelConverter) {
        this.sellerDao = sellerDao;
        this.modelConverter = modelConverter;
    }

    /**
     * Handles the request to retrieve a specific seller by their ID.
     * This method fetches the seller details from the database using {@link SellerDAO},
     * checks if the seller exists, and then converts the {@link Seller} entity to a {@link SellerModel}.
     *
     * @param request The {@link GetSellerRequest} containing the ID of the seller to be retrieved.
     * @return        A {@link GetSellerResult} object representing the outcome of the operation,
     *                including success status, a message, and the fetched seller model.
     */

    public GetSellerResult handleRequest(final GetSellerRequest request) {
        log.info("Received GetSellerRequest with sellerId: {}", request.getSellerId());

        // Fetch seller from the database
        Seller seller = sellerDao.getSeller(request.getSellerId());

        if (seller == null) {
            log.error("Seller not found with id: {}", request.getSellerId());
            return GetSellerResult.builder()
                    .withSuccess(false)
                    .withMessage("Seller not found.")
                    .build();
        }

        // Convert seller to seller model
        SellerModel sellerModel = modelConverter.toSellerModel(seller);

        return GetSellerResult.builder()
                .withSuccess(true)
                .withMessage("Seller fetched successfully.")
                .withSellerModel(sellerModel)
                .build();
    }
}
