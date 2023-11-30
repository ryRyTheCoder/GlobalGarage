package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.CreateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateSellerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.exceptions.InvalidAttributeValueException;
import com.nashss.se.GlobalGarage.models.SellerModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashSet;

import javax.inject.Inject;

/**
 * Implementation of the CreateSellerActivity for the GlobalGarage's CreateSeller API.
 * This API allows a seller to create an account.
 */
public class CreateSellerActivity {
    private final Logger log = LogManager.getLogger();
    private final SellerDAO sellerDao;
    private final ModelConverter modelConverter;

    /**
     * Instantiates a new CreateSellerActivity object.
     *
     * @param sellerDao to access the Seller table.
     * @param modelConverter to access the ModelConverter.
     */
    @Inject
    public CreateSellerActivity(SellerDAO sellerDao, ModelConverter modelConverter) {
        this.sellerDao = sellerDao;
        this.modelConverter = modelConverter;
    }

    /**
     * This method handles the incoming request by adding a seller to the database.
     * It then returns a CreateSellerResult.
     * <p>
     * If the seller already exists, an appropriate message should be returned.
     *
     * @param createSellerRequest request object to create a seller
     * @return createSellerResult result object indicating the outcome of the creation process
     */
    public CreateSellerResult handleRequest(final CreateSellerRequest createSellerRequest) {
        log.info("Received CreateSellerRequest {}", createSellerRequest);

        String username = createSellerRequest.getUsername();

        // Check for invalid characters in the username
        if (!username.matches("[a-zA-Z0-9 ]*")) {
            throw new InvalidAttributeValueException("Invalid characters in the seller username.");
        }

        String accountTypeWithUUID = "S" + createSellerRequest.getSellerId();

        Seller seller = new Seller();
        seller.setSellerID(accountTypeWithUUID);
        seller.setUsername(username);
        seller.setEmail(createSellerRequest.getEmail());
        seller.setLocation(createSellerRequest.getLocation());
        seller.setContactInfo(createSellerRequest.getContactInfo());
        seller.setGarages(new HashSet<>());
        seller.setMessages(new HashSet<>());
        seller.setSignupDate(LocalDateTime.now());

        // Set garages and messages to null if they are empty
        if (seller.getGarages().isEmpty()) {
            seller.setGarages(null);
        }
        if (seller.getMessages().isEmpty()) {
            seller.setMessages(null);
        }

        boolean success = sellerDao.createSeller(seller);
        String message = success ? "Seller created successfully." : "Failed to create seller.";

        SellerModel sellerModel = modelConverter.toSellerModel(seller);

        return CreateSellerResult.builder()
                .withSuccess(success)
                .withMessage(message)
                .withSellerModel(sellerModel)
                .build();
    }
}
