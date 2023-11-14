package com.nashss.se.GlobalGarage.activity;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nashss.se.GlobalGarage.activity.request.GetAllGaragesRequest;
import com.nashss.se.GlobalGarage.activity.results.GetAllGaragesResult;
import com.nashss.se.GlobalGarage.converters.ModelConverter;
import com.nashss.se.GlobalGarage.dynamodb.GarageDAO;
import com.nashss.se.GlobalGarage.dynamodb.models.Garage;
import com.nashss.se.GlobalGarage.models.GarageModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.Base64;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetAllGaragesActivity {
    private final Logger log = LogManager.getLogger();
    private final GarageDAO garageDao;
    private final ModelConverter modelConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public GetAllGaragesActivity(GarageDAO garageDao, ModelConverter modelConverter) {
        this.garageDao = garageDao;
        this.modelConverter = modelConverter;
    }

    public GetAllGaragesResult handleRequest(final GetAllGaragesRequest request) {
        log.info("Received GetAllGaragesRequest with lastEvaluatedKey: {}", request.getLastEvaluatedKey());

        // Decode the lastEvaluatedKey if present
        Map<String, AttributeValue> lastEvaluatedKeyDecoded = decodeLastEvaluatedKey(request.getLastEvaluatedKey());

        // Fetch garages from the database
        List<Garage> garages = garageDao.getAllGarages(lastEvaluatedKeyDecoded);
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

    private String encodeLastEvaluatedKey(Map<String, AttributeValue> lastEvaluatedKey) {
        if (lastEvaluatedKey == null) return null;
        try {
            String json = objectMapper.writeValueAsString(lastEvaluatedKey);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            log.error("Error encoding lastEvaluatedKey", e);
            throw new RuntimeException("Error encoding lastEvaluatedKey", e);
        }
    }

    public Map<String, AttributeValue> decodeLastEvaluatedKey(String encodedKey) {
        if (encodedKey == null || encodedKey.isEmpty()) return null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedKey);
            String json = new String(decodedBytes, StandardCharsets.UTF_8);
            return objectMapper.readValue(json, new TypeReference<Map<String, AttributeValue>>() {});
        } catch (IOException e) {
            log.error("Error decoding lastEvaluatedKey", e);
            throw new RuntimeException("Error decoding lastEvaluatedKey", e);
        }
    }
}
