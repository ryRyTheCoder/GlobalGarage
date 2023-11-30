package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.CreateGarageRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateGarageResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.models.GarageModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
/**
 * Implementation of the CreateGarageActivity for the GlobalGarage's CreateGarage API.
 * This API allows a seller to create a garage event.
 */
public class CreateGarageActivity {
    private final Logger log = LogManager.getLogger();
    private final GarageDAO garageDao;
    private final SellerDAO sellerDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new CreateGarageActivity object.
     *
     * @param garageDao to access the Garage table.
     * @param sellerDao to access the Seller table.
     * @param modelConverter to convert entities to models.
     */

    @Inject
    public CreateGarageActivity(GarageDAO garageDao, SellerDAO sellerDao, ModelConverter modelConverter) {
        this.garageDao = garageDao;
        this.sellerDao = sellerDao;
        this.modelConverter = modelConverter;
    }

    /**
     * This method handles the incoming request by creating a new garage event.
     *
     * @param createGarageRequest request object to create a garage
     * @return createGarageResult result object indicating the outcome of the creation process
     */
    public CreateGarageResult handleRequest(final CreateGarageRequest createGarageRequest) {
        log.info("Received CreateGarageRequest {}", createGarageRequest);

        String garageID = UUID.randomUUID().toString();
        String sellerID = createGarageRequest.getSellerID();

        Garage garage = new Garage();
        garage.setSellerID(sellerID);
        garage.setGarageID(garageID);
        garage.setGarageName(createGarageRequest.getGarageName());
        garage.setStartDate(createGarageRequest.getStartDate());
        garage.setEndDate(createGarageRequest.getEndDate());
        garage.setLocation(createGarageRequest.getLocation());
        garage.setDescription(createGarageRequest.getDescription());
        garage.setItems(new ArrayList<>());
        garage.setIsActive(true);

        boolean success = garageDao.createGarage(garage);
        if (success) {
            // If garage creation is successful, update the seller's garages
            success = updateSellerGarages(sellerID, garageID);
        }

        String message = success ? "Garage created successfully." : "Failed to create garage.";

        GarageModel garageModel = modelConverter.toGarageModel(garage);

        return CreateGarageResult.builder()
                .withSuccess(success)
                .withMessage(message)
                .withGarageModel(garageModel)
                .build();
    }

    private boolean updateSellerGarages(String sellerID, String garageID) {
        try {
            Seller seller = sellerDao.getSeller(sellerID);
            Set<String> garages = seller.getGarages();
            if (garages == null) {
                garages = new HashSet<>();
            }
            garages.add(garageID);
            seller.setGarages(garages);
            sellerDao.updateSeller(seller);
            return true;
        } catch (Exception e) {
            log.error("Error updating seller's garages", e);
            return false;
        }
    }
}
