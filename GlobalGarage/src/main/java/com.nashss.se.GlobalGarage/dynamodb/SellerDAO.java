package com.nashss.se.GlobalGarage.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.exceptions.SellerNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;



@Singleton
public class SellerDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();

    /**
     * Instantiates a SellerDAO object.
     *
     * @param dynamoDbMapper   the {@link DynamoDBMapper} used to interact with the sellers table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public SellerDAO(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.mapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }
    /**
     * Creates a seller in the database.
     *
     * @param seller the seller object to be stored
     * @return true if the seller is successfully created, false otherwise
     */
    public boolean createSeller(Seller seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Seller cannot be null");
        }

        try {
            mapper.save(seller);
            metricsPublisher.addCount(MetricsConstants.CREATE_SELLER_SUCCESS_COUNT, 1);
            return true;
        } catch (Exception e) {
            log.error("Error creating seller", e);
            metricsPublisher.addCount(MetricsConstants.CREATE_SELLER_FAIL_COUNT, 1);
            return false;
        }
    }

    public Seller getSeller(String sellerID){
        Seller seller = this.mapper.load(Seller.class, sellerID);

        if (seller == null) {
            metricsPublisher.addCount(MetricsConstants.SELLER_NOTFOUND_COUNT, 1);
            throw new SellerNotFoundException("Could not find seller with ID: " + sellerID);
        }
        metricsPublisher.addCount(MetricsConstants.SELLER_NOTFOUND_COUNT, 0);
        return seller;
    }
    public boolean updateSeller(Seller seller) {
        try {
            mapper.save(seller);
            metricsPublisher.addCount(MetricsConstants.UPDATE_SELLER_SUCCESS_COUNT, 1);
            log.info("Seller updated successfully: {}", seller.getSellerID());
            return true;
        } catch (Exception e) {
            metricsPublisher.addCount(MetricsConstants.UPDATE_SELLER_FAIL_COUNT, 1);
            log.error("Error updating seller: {}", seller.getSellerID(), e);
            return false;
        }
    }
}