package com.nashss.se.GlobalGarage.activity;

import com.nashss.se.GlobalGarage.activity.request.GetAllGaragesRequest;
import com.nashss.se.GlobalGarage.activity.results.GetAllGaragesResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.models.GarageModel;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class GetAllGaragesActivity {
    private final Logger log = LogManager.getLogger();
    private final GarageDAO garageDao;
    private final ModelConverter modelConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a GetAllGaragesActivity instance.
     *
     * @param garageDao      The {@link GarageDAO} used for accessing and manipulating garage data.
     * @param modelConverter The {@link ModelConverter} used for converting {@link Garage}
     *                       objects to {@link GarageModel} objects.
     */

    @Inject
    public GetAllGaragesActivity(GarageDAO garageDao, ModelConverter modelConverter) {
        this.garageDao = garageDao;
        this.modelConverter = modelConverter;
    }

    /**
     * Handles the request to retrieve all garages.
     * This method fetches garages from the database, converts them to garage models, and returns the result.
     * It supports pagination through lastEvaluatedKey.
     *
     * @param request The {@link GetAllGaragesRequest} containing optional pagination parameters.
     * @return        A {@link GetAllGaragesResult} object representing the outcome of the operation,
     *                including success status, a message, and the list of fetched garage models.
     */

    public GetAllGaragesResult handleRequest(final GetAllGaragesRequest request) {
        log.info("Received GetAllGaragesRequest with lastEvaluatedKey: {} and limit: {}",
                request.getLastEvaluatedKey(), request.getLimit());

        // Decode the lastEvaluatedKey if present
        Map<String, AttributeValue> lastEvaluatedKeyDecoded = decodeLastEvaluatedKey(request.getLastEvaluatedKey());

        // Check if limit is set and fetch garages accordingly
        List<Garage> garages;
        if (request.getLimit() != null && request.getLimit() > 0) {
            garages = garageDao.getAllGaragesWithLimit(lastEvaluatedKeyDecoded, request.getLimit());
        } else {
            garages = garageDao.getAllGarages(lastEvaluatedKeyDecoded);
        }

        Map<String, AttributeValue> newLastEvaluatedKey = garageDao.getLastEvaluatedKey();

        // Convert garages to garage models
        List<GarageModel> garageModels = garages.stream()
                .map(modelConverter::toGarageModel)
                .collect(Collectors.toList());

        // Encode the newLastEvaluatedKey
        String newLastEvaluatedKeyEncoded = encodeLastEvaluatedKey(newLastEvaluatedKey);
        boolean success = !garages.isEmpty();

        return GetAllGaragesResult.builder()
                .withSuccess(success)
                .withMessage(success ? "Garages fetched successfully." : "Failed to fetch garages.")
                .withGarageModels(garageModels)
                .withLastEvaluatedKey(newLastEvaluatedKeyEncoded)
                .build();
    }

    /**
     * Encodes a last evaluated key map to a Base64 string for use in pagination.
     * Throws a RuntimeException if encoding fails.
     *
     * @param lastEvaluatedKey The map of the last evaluated key to encode.
     * @return                 The encoded string representation of the last evaluated key.
     * @throws RuntimeException if encoding fails.
     */

    private String encodeLastEvaluatedKey(Map<String, AttributeValue> lastEvaluatedKey) {
        if (lastEvaluatedKey == null) {
            return null;
        }
        try {
            String json = objectMapper.writeValueAsString(lastEvaluatedKey);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            log.error("Error encoding lastEvaluatedKey", e);
            throw new RuntimeException("Error encoding lastEvaluatedKey", e);
        }
    }

    /**
     * Decodes a Base64 encoded string back to a map of last evaluated keys for use in pagination.
     * Throws a RuntimeException if decoding fails.
     *
     * @param encodedKey The Base64 encoded string representation of the last evaluated key.
     * @return           The decoded map of the last evaluated key.
     * @throws RuntimeException if decoding fails.
     */

    public Map<String, AttributeValue> decodeLastEvaluatedKey(String encodedKey) {
        if (encodedKey == null || encodedKey.isEmpty()) {
            return null;
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedKey);
            String json = new String(decodedBytes, StandardCharsets.UTF_8);
            return objectMapper.readValue(json, new TypeReference<Map<String, AttributeValue>>() { });
        } catch (IOException e) {
            log.error("Error decoding lastEvaluatedKey", e);
            throw new RuntimeException("Error decoding lastEvaluatedKey", e);
        }
    }
}
