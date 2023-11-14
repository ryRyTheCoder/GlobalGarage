package com.nashss.se.GlobalGarage.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;

import com.nashss.se.GlobalGarage.exceptions.GarageNotFoundException;

import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Base64;

import java.util.List;
import java.util.Map;

@Singleton
public class GarageDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();
    private Map<String, AttributeValue> lastEvaluatedKey = null;
    /**
     * Instantiates a GarageDAO object.
     *
     * @param dynamoDbMapper   the {@link DynamoDBMapper} used to interact with the garages table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public GarageDAO(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.mapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Creates a garage in the database.
     *
     * @param garage the garage object to be stored
     * @return true if the garage is successfully created, false otherwise
     */
    public boolean createGarage(Garage garage) {
        if (garage == null) {
            throw new IllegalArgumentException("Garage cannot be null");
        }

        try {
            mapper.save(garage);
            metricsPublisher.addCount(MetricsConstants.CREATE_GARAGE_SUCCESS_COUNT, 1);
            return true;
        } catch (Exception e) {
            log.error("Error creating garage", e);
            metricsPublisher.addCount(MetricsConstants.CREATE_GARAGE_FAIL_COUNT, 1);
            return false;
        }
    }

    public Garage getGarage(String sellerID, String garageID){

    try {
        Garage garageKey = new Garage();
        garageKey.setSellerID(sellerID);
        garageKey.setGarageID(garageID);

        Garage garage = mapper.load(garageKey);
        if (garage == null) {
            metricsPublisher.addCount(MetricsConstants.GARAGE_NOTFOUND_COUNT, 1);
            throw new GarageNotFoundException("Could not find garage with SellerID: " + sellerID + " and GarageID: " + garageID);
        }
        metricsPublisher.addCount(MetricsConstants.GARAGE_NOTFOUND_COUNT, 0);
        return garage;
    } catch (Exception e) {
        log.error("Error loading garage", e);
        throw e; // Rethrow the exception or handle it as per your application's requirement
    }
}


    public boolean updateGarage(Garage garage) {
        try {
            mapper.save(garage);
            metricsPublisher.addCount(MetricsConstants.UPDATE_GARAGE_SUCCESS_COUNT, 1);
            log.info("Garage updated successfully: {}", garage.getGarageID());
            return true;
        } catch (Exception e) {
            metricsPublisher.addCount(MetricsConstants.UPDATE_GARAGE_FAIL_COUNT, 1);
            log.error("Error updating garage: {}", garage.getGarageID(), e);
            return false;
        }
    }

    public List<Garage> getAllGarages(Map<String, AttributeValue> lastEvaluatedKey) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withLimit(10); // Limit the number of items returned

        if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {
            scanExpression.setExclusiveStartKey(lastEvaluatedKey);
        }

        ScanResultPage<Garage> scanResult = mapper.scanPage(Garage.class, scanExpression);
        this.lastEvaluatedKey = scanResult.getLastEvaluatedKey();

        return scanResult.getResults();
    }


    public Map<String, AttributeValue> getLastEvaluatedKey() {
        return this.lastEvaluatedKey;
    }
}
