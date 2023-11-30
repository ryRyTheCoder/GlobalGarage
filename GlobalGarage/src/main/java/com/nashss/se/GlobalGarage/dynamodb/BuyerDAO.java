package com.nashss.se.GlobalGarage.dynamodb;

import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.exceptions.BuyerNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles database operations for buyers using {@link Buyer} to represent the model in DynamoDB.
 */

@Singleton
public class BuyerDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();

    /**
     * Instantiates a BuyerDAO object.
     *
     * @param dynamoDbMapper    the {@link DynamoDBMapper} used for interactions with DynamoDB.
     * @param metricsPublisher  the {@link MetricsPublisher} used for recording metrics.
     */

    @Inject
    public BuyerDAO(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.mapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Creates a new buyer record in the database.
     *
     * @param buyer  the {@link Buyer} object to be stored.
     * @return       true if creation is successful, false otherwise.
     * @throws IllegalArgumentException if the buyer is null.
     */

    public boolean createBuyer(Buyer buyer) {
        if (buyer == null) {
            throw new IllegalArgumentException("Buyer cannot be null");
        }

        try {
            mapper.save(buyer);
            metricsPublisher.addCount(MetricsConstants.CREATE_BUYER_SUCCESS_COUNT, 1);
            return true;
        } catch (Exception e) {
            log.error("Error creating buyer", e);
            metricsPublisher.addCount(MetricsConstants.CREATE_BUYER_FAIL_COUNT, 1);
            return false;
        }
    }

    /**
     * Retrieves a buyer by their ID.
     *
     * @param buyerID  the ID of the buyer to retrieve.
     * @return         the {@link Buyer} object retrieved from the database.
     * @throws BuyerNotFoundException if no buyer is found with the given ID.
     */

    public Buyer getBuyer(String buyerID) {
        Buyer buyer = this.mapper.load(Buyer.class, buyerID);

        if (buyer == null) {
            metricsPublisher.addCount(MetricsConstants.BUYER_NOTFOUND_COUNT, 1);
            throw new BuyerNotFoundException("Could not find buyer with ID: " + buyerID);
        }
        metricsPublisher.addCount(MetricsConstants.BUYER_NOTFOUND_COUNT, 0);
        return buyer;
    }

    /**
     * Updates an existing buyer's information in the database.
     *
     * @param buyer  the {@link Buyer} object to be updated.
     * @return       true if the update is successful, false otherwise.
     */

    public boolean updateBuyer(Buyer buyer) {
        try {
            mapper.save(buyer);
            metricsPublisher.addCount(MetricsConstants.UPDATE_BUYER_SUCCESS_COUNT, 1);
            log.info("Buyer updated successfully: {}", buyer.getBuyerID());
            return true;
        } catch (Exception e) {
            metricsPublisher.addCount(MetricsConstants.UPDATE_BUYER_FAIL_COUNT, 1);
            log.error("Error updating buyer: {}", buyer.getBuyerID(), e);
            return false;
        }
    }
}
