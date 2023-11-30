package com.nashss.se.GlobalGarage.dynamodb;

import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.dynamodb.models.Message;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.exceptions.MessageNotFoundException;
import com.nashss.se.GlobalGarage.metrics.MetricsConstants;
import com.nashss.se.GlobalGarage.metrics.MetricsPublisher;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles database operations for messages using {@link Message} to represent the model in DynamoDB.
 */

@Singleton
public class MessageDAO {
    private final DynamoDBMapper mapper;
    private final MetricsPublisher metricsPublisher;
    private final Logger log = LogManager.getLogger();

    /**
     * Instantiates a MessageDAO object.
     *
     * @param dynamoDbMapper    the {@link DynamoDBMapper} used for interactions with DynamoDB.
     * @param metricsPublisher  the {@link MetricsPublisher} used for recording metrics.
     */

    @Inject
    public MessageDAO(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.mapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Creates a new message record in the database.
     *
     * @param message  the {@link Message} object to be stored.
     * @return         true if creation is successful, false otherwise.
     * @throws IllegalArgumentException if the message is null.
     */

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

    /**
     * Retrieves a message by its ID.
     *
     * @param messageID  the ID of the message to retrieve.
     * @return           the {@link Message} object retrieved from the database.
     * @throws MessageNotFoundException if no message is found with the given ID.
     */

    public Message getMessage(String messageID) {
        Message message = this.mapper.load(Message.class, messageID);

        if (message == null) {
            metricsPublisher.addCount(MetricsConstants.MESSAGE_NOTFOUND_COUNT, 1);
            throw new MessageNotFoundException("Could not find message with ID: " + messageID);
        }
        metricsPublisher.addCount(MetricsConstants.MESSAGE_NOTFOUND_COUNT, 0);
        return message;
    }

    /**
     * Updates the message records associated with a user.
     *
     * @param senderID      the ID of the sender.
     * @param senderType    the type of the sender (buyer/seller).
     * @param receiverID    the ID of the receiver.
     * @param receiverType  the type of the receiver (buyer/seller).
     * @param messageID     the ID of the message to update.
     * @return              true if the update is successful for both sender and receiver, false otherwise.
     */

    public boolean updateUserMessages(String senderID, String senderType, String receiverID,
                                      String receiverType, String messageID) {

        boolean senderUpdateSuccess = updateMessagesForUser(senderID, senderType, messageID);
        boolean receiverUpdateSuccess = updateMessagesForUser(receiverID, receiverType, messageID);

        return senderUpdateSuccess && receiverUpdateSuccess;
    }

    /**
     * Updates the message list for a user based on their type (buyer or seller).
     *
     * @param userID    the ID of the user.
     * @param userType  the type of the user ("buyer" or "seller").
     * @param messageID the ID of the message to add to the user's message list.
     * @return          true if the message list is successfully updated, false otherwise.
     */

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

    /**
     * Adds a message ID to the message list of a buyer and updates the buyer record.
     *
     * @param buyer     the {@link Buyer} object to update.
     * @param messageID the message ID to add to the buyer's message list.
     * @return          true if the buyer's message list is successfully updated, false otherwise.
     */

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

    /**
     * Adds a message ID to the message list of a seller and updates the seller record.
     *
     * @param seller    the {@link Seller} object to update.
     * @param messageID the message ID to add to the seller's message list.
     * @return          true if the seller's message list is successfully updated, false otherwise.
     */

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
