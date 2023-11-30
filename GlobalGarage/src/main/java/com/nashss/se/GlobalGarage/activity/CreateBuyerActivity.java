package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.CreateBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateBuyerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.exceptions.InvalidAttributeValueException;
import com.nashss.se.GlobalGarage.models.BuyerModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashSet;

import javax.inject.Inject;


/**
 * Implementation of the CreateBuyerActivity for the GlobalGarage's CreateBuyer API.
 * This API allows a buyer to create an account.
 */
public class CreateBuyerActivity {
    private final Logger log = LogManager.getLogger();
    private final BuyerDAO buyerDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new CreateBuyerActivity object.
     *
     * @param buyerDao to access the Buyer table.
     * @param modelConverter to convert entities to models.
     */
    @Inject
    public CreateBuyerActivity(BuyerDAO buyerDao, ModelConverter modelConverter) {
        this.buyerDao = buyerDao;
        this.modelConverter = modelConverter;
    }

    /**
     * This method handles the incoming request by adding a buyer to the database.
     * It then returns a CreateBuyerResult.
     *
     * @param createBuyerRequest request object to create a buyer
     * @return createBuyerResult result object indicating the outcome of the creation process
     */
    public CreateBuyerResult handleRequest(final CreateBuyerRequest createBuyerRequest) {
        log.info("Received CreateBuyerRequest {}", createBuyerRequest);

        String username = createBuyerRequest.getUsername();
        String accountTypeWithUUID = "B" + createBuyerRequest.getBuyerId();
        // Check for invalid characters in the username
        if (!username.matches("[a-zA-Z0-9 ]*")) {
            throw new InvalidAttributeValueException("Invalid characters in the buyer username.");
        }

        Buyer buyer = new Buyer();
        buyer.setBuyerID(accountTypeWithUUID);
        buyer.setUsername(username);
        buyer.setEmail(createBuyerRequest.getEmail());
        buyer.setLocation(createBuyerRequest.getLocation());
        buyer.setItemsInterested(new HashSet<>());
        buyer.setMessages(new HashSet<>());
        buyer.setSignupDate(LocalDateTime.now());

        // Set itemsInterested and messages to null if they are empty
        if (buyer.getItemsInterested().isEmpty()) {
            buyer.setItemsInterested(null);
        }
        if (buyer.getMessages().isEmpty()) {
            buyer.setMessages(null);
        }

        boolean success = buyerDao.createBuyer(buyer);
        String message = success ? "Buyer created successfully." : "Failed to create buyer.";

        BuyerModel buyerModel = modelConverter.toBuyerModel(buyer);

        return CreateBuyerResult.builder()
                .withSuccess(success)
                .withMessage(message)
                .withBuyerModel(buyerModel)
                .build();
    }
}
