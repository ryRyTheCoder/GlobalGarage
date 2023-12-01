package com.nashss.se.globalgarage.activity;

import com.nashss.se.GlobalGarage.activity.CreateBuyerActivity;
import com.nashss.se.GlobalGarage.activity.request.CreateBuyerRequest;
import com.nashss.se.GlobalGarage.activity.results.CreateBuyerResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.BuyerDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Buyer;
import com.nashss.se.GlobalGarage.exceptions.InvalidAttributeValueException;
import com.nashss.se.GlobalGarage.models.BuyerModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

public class CreateBuyerActivityTest {
    @Mock
    private BuyerDAO buyerDao;
    @Mock
    private ModelConverter modelConverter;

    private CreateBuyerActivity createBuyerActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createBuyerActivity = new CreateBuyerActivity(buyerDao, modelConverter);
    }

    @Test
    public void handleRequest_validRequest_createsBuyer() {
        // GIVEN
        String username = "testUser";
        String email = "test@example.com";
        String location = "TestCity";
        String buyerId = "B1234";
        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .withUsername(username)
                .withEmail(email)
                .withLocation(location)
                .withBuyerId(buyerId)
                .build();

        Buyer dummyBuyer = new Buyer();
        dummyBuyer.setBuyerID(buyerId);
        dummyBuyer.setUsername(username);
        dummyBuyer.setEmail(email);
        dummyBuyer.setLocation(location);
        dummyBuyer.setItemsInterested(new HashSet<>());
        dummyBuyer.setMessages(new HashSet<>());

        // Setting up a dummy BuyerModel matching the expected conversion
        BuyerModel dummyBuyerModel = new BuyerModel(
                buyerId,
                username,
                email,
                location,
                new HashSet<>(),
                new HashSet<>(),
                LocalDateTime.now().toString()
        );

        Mockito.when(buyerDao.createBuyer(any(Buyer.class))).thenReturn(true);
        Mockito.when(modelConverter.toBuyerModel(any(Buyer.class))).thenReturn(dummyBuyerModel);

        // WHEN
        CreateBuyerResult result = createBuyerActivity.handleRequest(request);

        // THEN
        assertTrue(result.isSuccess());
        assertNotNull(result.getBuyerModel());
        assertEquals("Buyer created successfully.", result.getMessage());
        Mockito.verify(buyerDao).createBuyer(any(Buyer.class));

        // Additional assertions to verify the details of the BuyerModel
        BuyerModel resultModel = result.getBuyerModel();
        assertEquals(buyerId, resultModel.getBuyerID());
        assertEquals(username, resultModel.getUsername());
        assertEquals(email, resultModel.getEmail());
        assertEquals(location, resultModel.getLocation());
        assertTrue(resultModel.getItemsInterested().isEmpty());
        assertTrue(resultModel.getMessages().isEmpty());
    }

    @Test
    public void handleRequest_invalidUsername_throwsInvalidAttributeValueException() {
        // GIVEN
        String invalidUsername = "!nv@lidUser";
        String email = "test@example.com";
        String location = "TestCity";
        String buyerId = "1234";

        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .withUsername(invalidUsername)
                .withEmail(email)
                .withLocation(location)
                .withBuyerId(buyerId)
                .build();

        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class, () -> createBuyerActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_daoFailure_returnsFailureResult() {
        // GIVEN
        String username = "testUser";
        String email = "test@example.com";
        String location = "TestCity";
        String buyerId = "1234";

        CreateBuyerRequest request = CreateBuyerRequest.builder()
                .withUsername(username)
                .withEmail(email)
                .withLocation(location)
                .withBuyerId(buyerId)
                .build();

        Mockito.when(buyerDao.createBuyer(any(Buyer.class))).thenReturn(false);

        // WHEN
        CreateBuyerResult result = createBuyerActivity.handleRequest(request);

        // THEN
        assertFalse(result.isSuccess());
        assertEquals("Failed to create buyer.", result.getMessage());
        assertNull(result.getBuyerModel());
    }
}
