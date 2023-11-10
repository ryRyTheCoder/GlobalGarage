package com.nashss.se.GlobalGarage.converters;

import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.dynamodb.models.Seller;
import com.nashss.se.GlobalGarage.models.GarageModel;
import com.nashss.se.GlobalGarage.models.SellerModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Converter class to transform entity objects to models and vice versa.
 */
public class ModelConverter {

    /**
     * Converts a provided Seller into SellerModel representation.
     *
     * @param seller the Seller to convert to SellerModel
     * @return the converted SellerModel with fields mapped from seller
     */
    public SellerModel toSellerModel(Seller seller) {
        Set<String> eventIds = seller.getGarages() != null ? new HashSet<>(seller.getGarages()) : new HashSet<>();
        Set<String> messageIds = seller.getMessages() != null ? new HashSet<>(seller.getMessages()) : new HashSet<>();
        String signupDateString = seller.getSignupDate() != null ? seller.getSignupDate().toString() : null;


        return SellerModel.builder()
                .withSellerID(seller.getSellerID())
                .withUsername(seller.getUsername())
                .withEmail(seller.getEmail())
                .withLocation(seller.getLocation())
                .withContactInfo(seller.getContactInfo())
                .withGarages(eventIds)
                .withMessages(messageIds)
                .withSignupDate(signupDateString)
                .build();
    }

    /**
     * Converts a list of Sellers to a list of SellerModels.
     *
     * @param sellers The Sellers to convert to SellerModels
     * @return The converted list of SellerModels
     */
    public List<SellerModel> toSellerModelList(List<Seller> sellers) {
        List<SellerModel> sellerModels = new ArrayList<>();
        sellers.forEach(seller -> sellerModels.add(toSellerModel(seller)));
        return sellerModels;
    }
    public GarageModel toGarageModel(Garage garage) {

        List<String> itemsList = garage.getItems() != null ? new ArrayList<>(garage.getItems()) : new ArrayList<>();

        return GarageModel.builder()
                .withSellerID(garage.getSellerID())
                .withGarageID(garage.getGarageID())
                .withGarageName(garage.getGarageName())
                .withStartDate(garage.getStartDate())
                .withEndDate(garage.getEndDate())
                .withLocation(garage.getLocation())
                .withDescription(garage.getDescription())
                .withItems(itemsList)
                .withIsActive(garage.getIsActive())
                .build();
    }

}