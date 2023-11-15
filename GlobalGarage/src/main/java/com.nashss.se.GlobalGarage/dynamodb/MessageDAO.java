package com.nashss.se.GlobalGarage.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.dynamodb.models.Message;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.exceptions.MessageNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class MessageDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();

    @Inject
    public MessageDAO(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.mapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    public boolean createMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        try {
            mapper.save(message);
            metricsPublisher.addCount(MetricsConstants.CREATE_MESSAGE_SUCCESS_COUNT, 1);
            return true;
        } catch (Exception e) {
            log.error("Error creating message", e);
            metricsPublisher.addCount(MetricsConstants.CREATE_MESSAGE_FAIL_COUNT, 1);
            return false;
        }
    }

    public Message getMessage(String messageID) {
        Message message = this.mapper.load(Message.class, messageID);

        if (message == null) {
            metricsPublisher.addCount(MetricsConstants.MESSAGE_NOTFOUND_COUNT, 1);
            throw new MessageNotFoundException("Could not find message with ID: " + messageID);
        }
        metricsPublisher.addCount(MetricsConstants.MESSAGE_NOTFOUND_COUNT, 0);
        return message;
    }

    public boolean updateUserMessages(String senderID, String senderType, String receiverID, String receiverType, String messageID) {
        boolean senderUpdateSuccess = updateMessagesForUser(senderID, senderType, messageID);
        boolean receiverUpdateSuccess = updateMessagesForUser(receiverID, receiverType, messageID);

        return senderUpdateSuccess && receiverUpdateSuccess;
    }

    private boolean updateMessagesForUser(String userID, String userType, String messageID) {
        if ("buyer".equalsIgnoreCase(userType)) {
            Buyer buyer = mapper.load(Buyer.class, userID);
            if (buyer != null) {
                return updateMessagesForBuyer(buyer, messageID);
            }
        } else if ("seller".equalsIgnoreCase(userType)) {
            Seller seller = mapper.load(Seller.class, userID);
            if (seller != null) {
                return updateMessagesForSeller(seller, messageID);
            }
        }

        log.error("User not found or type is incorrect for ID: {}", userID);
        return false;
    }

    private boolean updateMessagesForBuyer(Buyer buyer, String messageID) {
        Set<String> messages = buyer.getMessages();
        if (messages == null) {
            messages = new HashSet<>();
        }
        messages.add(messageID);
        buyer.setMessages(messages);

        try {
            mapper.save(buyer);
            return true;
        } catch (Exception e) {
            log.error("Error updating messages for buyer: {}", buyer.getBuyerID(), e);
            return false;
        }
    }

    private boolean updateMessagesForSeller(Seller seller, String messageID) {
        Set<String> messages = seller.getMessages();
        if (messages == null) {
            messages = new HashSet<>();
        }
        messages.add(messageID);
        seller.setMessages(messages);

        try {
            mapper.save(seller);
            return true;
        } catch (Exception e) {
            log.error("Error updating messages for seller: {}", seller.getSellerID(), e);
            return false;
        }
    }
}

