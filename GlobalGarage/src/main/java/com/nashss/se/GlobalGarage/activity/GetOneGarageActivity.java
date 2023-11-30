package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.GetOneGarageRequest;
import com.nashss.se.GlobalGarage.activity.results.GetOneGarageResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.models.GarageModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class GetOneGarageActivity {
    private final Logger log = LogManager.getLogger();
    private final GarageDAO garageDao;
    private final ModelConverter modelConverter;

    /**
     * Constructs a GetOneGarageActivity instance.
     *
     * @param garageDao      The {@link GarageDAO} used for accessing and manipulating garage data.
     * @param modelConverter The {@link ModelConverter} used for converting between different model types.
     */

    @Inject
    public GetOneGarageActivity(GarageDAO garageDao, ModelConverter modelConverter) {
        this.garageDao = garageDao;
        this.modelConverter = modelConverter;
    }

    /**
     * Handles the request to retrieve a specific garage by its ID.
     * This method fetches the garage details from the database using {@link GarageDAO},
     * converts the {@link Garage} entity to a {@link GarageModel}, and returns the result.
     *
     * @param request The request object containing the seller ID and garage ID.
     * @return        A {@link GetOneGarageResult} object representing the outcome of the operation,
     *                including success status, a message, and the fetched garage model.
     */

    public GetOneGarageResult handleRequest(final GetOneGarageRequest request) {
        log.info("Received GetOneGarageRequest with garageId: {}", request.getGarageId());

        // Fetch garage from the database
        Garage garage = garageDao.getGarage(request.getSellerId(), request.getGarageId());

        if (garage == null) {
            log.error("Garage not found with id: {}", request.getGarageId());
            return GetOneGarageResult.builder()
                    .withSuccess(false)
                    .withMessage("Garage not found.")
                    .build();
        }
        // Convert garage to garage model
        GarageModel garageModel = modelConverter.toGarageModel(garage);

        return GetOneGarageResult.builder()
                .withSuccess(true)
                .withMessage("Garage fetched successfully.")
                .withGarageModel(garageModel)
                .build();
    }
}
