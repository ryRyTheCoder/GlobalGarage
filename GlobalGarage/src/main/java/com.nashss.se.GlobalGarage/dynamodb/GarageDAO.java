package com.nashss.se.GlobalGarage.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.exceptions.GarageNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GarageDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();

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
            metricsPublisher.addCount(MetricsConstants.CREATEGARAGE_SUCCESS_COUNT, 1);
            return true;
        } catch (Exception e) {
            log.error("Error creating garage", e);
            metricsPublisher.addCount(MetricsConstants.CREATEGARAGE_FAIL_COUNT, 1);
            return false;
        }
    }

    // Additional methods as required, like getGarage, updateGarage, deleteGarage, etc.
}
