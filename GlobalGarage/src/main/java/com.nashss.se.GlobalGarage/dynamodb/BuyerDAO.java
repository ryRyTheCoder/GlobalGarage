package com.nashss.se.GlobalGarage.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.exceptions.BuyerNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BuyerDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();

    @Inject
    public BuyerDAO(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.mapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

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

    public Buyer getBuyer(String buyerID) {
        Buyer buyer = this.mapper.load(Buyer.class, buyerID);

        if (buyer == null) {
            metricsPublisher.addCount(MetricsConstants.BUYER_NOTFOUND_COUNT, 1);
            throw new BuyerNotFoundException("Could not find buyer with ID: " + buyerID);
        }
        metricsPublisher.addCount(MetricsConstants.BUYER_NOTFOUND_COUNT, 0);
        return buyer;
    }

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
