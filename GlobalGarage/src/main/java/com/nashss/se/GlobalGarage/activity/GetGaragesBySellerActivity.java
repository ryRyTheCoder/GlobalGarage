package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.GetGaragesBySellerRequest;
import com.nashss.se.GlobalGarage.activity.results.GetGaragesBySellerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.models.GarageModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Implementation of the GetGaragesBySellerActivity for the GlobalGarage's GetGaragesBySeller API.
 * This API allows fetching all garages created by a specific seller.
 */
public class GetGaragesBySellerActivity {
    private final Logger log = LogManager.getLogger();
    private final GarageDAO garageDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new GetGaragesBySellerActivity object.
     *
     * @param garageDao to access the Garage table.
     * @param modelConverter to convert entities to models.
     */
    @Inject
    public GetGaragesBySellerActivity(GarageDAO garageDao, ModelConverter modelConverter) {
        this.garageDao = garageDao;
        this.modelConverter = modelConverter;
    }

    /**
     * This method handles the incoming request by fetching all garages for a specific seller.
     *
     * @param request request object to get garages by a seller
     * @return result object indicating the outcome of the fetch process
     */
    public GetGaragesBySellerResult handleRequest(final GetGaragesBySellerRequest request) {
        log.info("Received GetGaragesBySellerRequest {}", request);

        try {
            List<Garage> garages = garageDao.getGaragesBySeller(request.getSellerId(), request.getLimit(),
                    request.getLastEvaluatedKey());
            List<GarageModel> garageModels = garages.stream()
                    .map(modelConverter::toGarageModel)
                    .collect(Collectors.toList());

            return GetGaragesBySellerResult.builder()
                    .withSuccess(true)
                    .withMessage("Garages fetched successfully.")
                    .withGarageModels(garageModels)
                    .build();
        } catch (Exception e) {
            log.error("Error fetching garages for seller {}", request.getSellerId(), e);
            return GetGaragesBySellerResult.builder()
                    .withSuccess(false)
                    .withMessage("Failed to fetch garages.")
                    .build();
        }
    }
}
