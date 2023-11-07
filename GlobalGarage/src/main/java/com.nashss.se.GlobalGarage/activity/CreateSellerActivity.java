package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.CreateSellerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateSellerResult;
import com.nashss.se.GlobalGarage.dynamodb.SellerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.exceptions.InvalidAttributeValueException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;


/**
 * Implementation of the CreateSellerActivity for the GlobalGarage's CreateSeller API.
 * This API allows a seller to create an account.
 */
public class CreateSellerActivity {
    private final Logger log = LogManager.getLogger();
    private final SellerDAO sellerDao;

    /**
     * Instantiates a new CreateSellerActivity object.
     *
     * @param sellerDao to access the Seller table.
     */
    @Inject
    public CreateSellerActivity(SellerDAO sellerDao) {
        this.sellerDao = sellerDao;
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


        // Auto-generate a unique seller ID
        String sellerID = UUID.randomUUID().toString();

        Seller seller = new Seller();
        seller.setSellerID(sellerID);
        seller.setUsername(username);
        seller.setEmail(createSellerRequest.getEmail());
        seller.setLocation(createSellerRequest.getLocation());
        seller.setContactInfo(createSellerRequest.getContactInfo());
        seller.setGarages(null);
        seller.setSignupDate(LocalDateTime.now());

        boolean success = sellerDao.createSeller(seller);
        String message = success ? "Seller created successfully." : "Failed to create seller.";

        return CreateSellerResult.builder()
                .withSuccess(success)
                .withMessage(message)
                .withSellerID(seller.getSellerID())
                .build();
    }
}